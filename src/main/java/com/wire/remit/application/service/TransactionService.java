package com.wire.remit.application.service;

import com.wire.remit.application.dto.command.DepositCommand;
import com.wire.remit.application.dto.command.TransferCommand;
import com.wire.remit.application.dto.command.WithdrawCommand;
import com.wire.remit.application.dto.query.TransactionQuery;
import com.wire.remit.application.dto.result.TransactionHistoryResult;
import com.wire.remit.application.dto.result.TransactionResult;
import com.wire.remit.application.mapper.TransactionAppMapper;
import com.wire.remit.application.port.in.TransactionUseCase;
import com.wire.remit.application.port.out.LoadAccountPort;
import com.wire.remit.application.port.out.LoadTransactionPort;
import com.wire.remit.application.port.out.SaveAccountPort;
import com.wire.remit.application.port.out.SaveTransactionPort;
import com.wire.remit.application.port.out.contract.SearchTransactionProjection;
import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.Money;
import com.wire.remit.domain.account.service.DailyLimitPolicy;
import com.wire.remit.domain.account.service.FeePolicy;
import com.wire.remit.domain.common.DomainException;
import com.wire.remit.domain.common.ErrorCode;
import com.wire.remit.domain.transaction.model.Transaction;
import com.wire.remit.domain.transaction.model.TransactionType;
import com.wire.remit.domain.transaction.service.TransferDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService implements TransactionUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final SaveTransactionPort saveTransactionPort;
    private final LoadTransactionPort loadTransactionPort;
    private final DailyLimitPolicy dailyLimitPolicy;
    private final FeePolicy feePolicy;
    private final TransferDomainService transferDomainService;
    private final TransactionAppMapper mapper;


    /**
     * 계좌 입금
     * 1. 계좌조회 -> 입금처리 -> 잔액변경 -> 계좌 저장
     * 2. 거래내역 생성 -> 거래내역 저장
     */
    @Override
    public TransactionResult deposit(DepositCommand command) {
        Account account = loadAccountPort.findById(command.accountId())
                .orElseThrow(() -> new DomainException(ErrorCode.ACCOUNT_NOT_FOUND));
        account.deposit(Money.of(command.amount()));
        saveAccountPort.save(account);

        Transaction transaction = mapper.toDomain(account.getId(), command.amount(), TransactionType.DEPOSIT);
        saveTransactionPort.save(transaction);
        return mapper.toResult(transaction);
    }

    /**
     * 계좌 출금
     * 1. 계좌조회 -> 출금처리(한도체크) -> 잔액변경 -> 계좌 저장
     * 2. 거래내역 생성 -> 거래내역 저장
     */
    @Override
    public TransactionResult withdraw(WithdrawCommand command) {
        Account account = loadAccountPort.findById(command.accountId())
                .orElseThrow(() -> new DomainException(ErrorCode.ACCOUNT_NOT_FOUND));
        BigDecimal todaySum = loadTransactionPort.calculateTodayAmount(account.getId(), TransactionType.WITHDRAW);
        dailyLimitPolicy.checkWithdrawLimit(Money.of(todaySum), Money.of(command.amount()));
        account.withdraw(Money.of(command.amount()));
        saveAccountPort.save(account);

        Transaction transaction = mapper.toDomain(account.getId(), command.amount(), TransactionType.WITHDRAW);
        saveTransactionPort.save(transaction);

        return mapper.toResult(transaction);
    }

    /**
     * 계좌 이체
     * 1. 출금계좌, 입금계좌 조회 -> 출금계좌 한도체크 -> 출금계좌, 입금계좌 잔액변경 -> 계좌 저장
     * 2. 출금거래내역, 수수료거래내역, 입금거래내역 생성 -> 거래내역 저장
     */
    @Override
    public TransactionResult transfer(TransferCommand command) {
        // account section
        Account source = loadAccountPort.findById(command.sourceAccountId())
                .orElseThrow(() -> new DomainException(ErrorCode.ACCOUNT_NOT_FOUND, "출금계좌 없음"));
        Account target = loadAccountPort.findById(command.targetAccountId())
                .orElseThrow(() -> new DomainException(ErrorCode.ACCOUNT_NOT_FOUND, "입금게좌 없음"));

        BigDecimal todaySum = loadTransactionPort.calculateTodayAmount(source.getId(), TransactionType.TRANSFER_OUT);
        dailyLimitPolicy.checkTransferLimit(Money.of(todaySum), Money.of(command.amount()));

        Money fee = feePolicy.transferFee(Money.of(command.amount()));
        transferDomainService.transfer(source, target, Money.of(command.amount()), fee);

        saveAccountPort.save(source);
        saveAccountPort.save(target);

        // transaction section
        Transaction outTx = mapper.toDomain(source.getId(), command.amount(), TransactionType.TRANSFER_OUT);
        Transaction feeTx = mapper.toDomain(source.getId(), command.amount(), TransactionType.FEE);
        Transaction inTx = mapper.toDomain(target.getId(), command.amount(), TransactionType.TRANSFER_IN);

        saveTransactionPort.save(outTx);
        saveTransactionPort.save(feeTx);
        saveTransactionPort.save(inTx);

        return mapper.toResultWithFee(outTx, fee.getAmount().longValue());
    }

    /**
     * 계좌 거래내역 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionHistoryResult getByAccount(TransactionQuery query) {
        var criteria = mapper.toCriteria(query);
        List<SearchTransactionProjection> list = loadTransactionPort.loadByAccountInRange(criteria);
        return mapper.toResult(list);
    }
}
