package com.wire.remit.application.service;

import com.wire.remit.application.dto.command.DepositCommand;
import com.wire.remit.application.dto.command.TransferCommand;
import com.wire.remit.application.dto.command.WithdrawCommand;
import com.wire.remit.application.dto.query.TransactionQuery;
import com.wire.remit.application.dto.result.TransactionHistoryResult;
import com.wire.remit.application.dto.result.TransactionResult;
import com.wire.remit.application.mapper.TransactionAppMapper;
import com.wire.remit.application.port.out.LoadAccountPort;
import com.wire.remit.application.port.out.LoadTransactionPort;
import com.wire.remit.application.port.out.SaveAccountPort;
import com.wire.remit.application.port.out.SaveTransactionPort;
import com.wire.remit.application.port.out.contract.SearchTransactionCriteria;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionService 단위 테스트")
class TransactionServiceTest {

    @Mock private LoadAccountPort loadAccountPort;
    @Mock private SaveAccountPort saveAccountPort;
    @Mock private SaveTransactionPort saveTransactionPort;
    @Mock private LoadTransactionPort loadTransactionPort;
    @Mock private DailyLimitPolicy dailyLimitPolicy;
    @Mock private FeePolicy feePolicy;
    @Mock private TransferDomainService transferDomainService;
    @Mock private TransactionAppMapper mapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("입금 성공")
    void deposit_success() {
        Long amount = 1_000_000L;
        // given
        DepositCommand command = DepositCommand.builder()
                .accountId(1L).amount(amount).build();

        Account account = Account.builder()
                .id(1L).ownerName("김큰손").accountName("사업통장").balance(Money.of(amount)).build();

        Transaction tx = Transaction.builder()
                .accountId(1L).money(Money.of(amount)).type(TransactionType.DEPOSIT).build();

        TransactionResult expected = TransactionResult.builder()
                .accountId(1L).amount(amount).type(TransactionType.DEPOSIT).build();

        given(loadAccountPort.findById(1L)).willReturn(Optional.of(account));
        given(mapper.toDomain(1L, amount, TransactionType.DEPOSIT)).willReturn(tx);
        given(mapper.toResult(tx)).willReturn(expected);

        // when
        TransactionResult result = transactionService.deposit(command);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

        then(loadAccountPort).should(times(1)).findById(1L);
        then(saveAccountPort).should(times(1)).save(account);
        then(saveTransactionPort).should(times(1)).save(tx);
    }

    @Test
    @DisplayName("입금 실패 - 계좌 없음")
    void deposit_fail_accountNotFound() {
        // given
        DepositCommand command = DepositCommand.builder()
                .accountId(99L).amount(10_000L).build();

        given(loadAccountPort.findById(99L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> transactionService.deposit(command))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());

        then(loadAccountPort).should(times(1)).findById(99L);
        then(saveAccountPort).shouldHaveNoInteractions();
        then(saveTransactionPort).shouldHaveNoInteractions();
    }

    // -----------------------------------------------------------
    // [출금]
    // -----------------------------------------------------------
    @Test
    @DisplayName("출금 성공")
    void withdraw_success() {
        var balance = 1_000_000L;
        var amount = 500_000L;
        var balanaceMoney = Money.of(balance);
        var amountMoney = Money.of(amount);
        // given
        WithdrawCommand command = WithdrawCommand.builder()
                .accountId(1L).amount(amount).build();

        Account account = Account.builder()
                .id(1L).ownerName("김큰손").accountName("사업통장").balance(balanaceMoney).build();

        Transaction tx = Transaction.builder()
                .accountId(1L).money(amountMoney).type(TransactionType.WITHDRAW).build();

        TransactionResult expected = TransactionResult.builder()
                .accountId(1L).amount(amount).type(TransactionType.WITHDRAW).build();

        given(loadAccountPort.findById(1L)).willReturn(Optional.of(account));
        given(loadTransactionPort.calculateTodayAmount(1L, TransactionType.WITHDRAW)).willReturn(BigDecimal.ZERO);
        willDoNothing().given(dailyLimitPolicy).checkWithdrawLimit(any(), any());
        given(mapper.toDomain(1L, amount, TransactionType.WITHDRAW)).willReturn(tx);
        given(mapper.toResult(tx)).willReturn(expected);

        // when
        TransactionResult result = transactionService.withdraw(command);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

        then(loadAccountPort).should(times(1)).findById(1L);
        then(saveAccountPort).should(times(1)).save(account);
        then(saveTransactionPort).should(times(1)).save(tx);
    }

    @Test
    @DisplayName("출금 실패 - 계좌 없음")
    void withdraw_fail_accountNotFound() {
        // given
        WithdrawCommand command = WithdrawCommand.builder()
                .accountId(99L).amount(500_000L).build();

        given(loadAccountPort.findById(99L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> transactionService.withdraw(command))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());

        then(loadAccountPort).should(times(1)).findById(99L);
        then(saveAccountPort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("이체 성공")
    void transfer_success() {
        var amount = 500_000L;
        var fee = 5_000L;
        var amountMoney = Money.of(amount);
        var feeMoney = Money.of(fee);

        // given
        TransferCommand command = TransferCommand.builder()
                .sourceAccountId(1L).targetAccountId(2L).amount(amount).build();

        Account source = Account.builder().id(1L).ownerName("출금자").accountName("출금통장").build();
        Account target = Account.builder().id(2L).ownerName("입금자").accountName("입금통장").build();

        Transaction outTx = Transaction.builder()
                .accountId(1L).money(amountMoney).type(TransactionType.TRANSFER_OUT).build();

        Transaction feeTx = Transaction.builder()
                .accountId(1L).money(feeMoney).type(TransactionType.FEE).build();

        Transaction inTx = Transaction.builder()
                .accountId(2L).money(amountMoney).type(TransactionType.TRANSFER_IN).build();

        TransactionResult expected = TransactionResult.builder()
                .accountId(1L).amount(amount).type(TransactionType.TRANSFER_OUT).build();

        given(loadAccountPort.findById(1L)).willReturn(Optional.of(source));
        given(loadAccountPort.findById(2L)).willReturn(Optional.of(target));
        given(loadTransactionPort.calculateTodayAmount(1L, TransactionType.TRANSFER_OUT)).willReturn(BigDecimal.ZERO);
        willDoNothing().given(dailyLimitPolicy).checkTransferLimit(any(), any());
        given(feePolicy.transferFee(any(Money.class))).willReturn(feeMoney);


        given(mapper.toDomain(1L, amount, TransactionType.TRANSFER_OUT)).willReturn(outTx);
        given(mapper.toDomain(1L, amount, TransactionType.FEE)).willReturn(feeTx);
        given(mapper.toDomain(2L, amount, TransactionType.TRANSFER_IN)).willReturn(inTx);
        given(mapper.toResultWithFee(outTx, fee)).willReturn(expected);

        // when
        TransactionResult result = transactionService.transfer(command);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

        then(loadAccountPort).should(times(1)).findById(1L);
        then(loadAccountPort).should(times(1)).findById(2L);
        then(saveAccountPort).should(times(1)).save(source);
        then(saveAccountPort).should(times(1)).save(target);
        then(saveTransactionPort).should(times(3)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("거래내역 조회 성공")
    void getByAccount_success() {
        var from = LocalDateTime.of(2025, 10, 1, 0, 0);
        var to = LocalDateTime.of(2025, 10, 9, 23, 59, 59);
        // given
        TransactionQuery query = TransactionQuery.builder()
                .accountId(1L).from(from).to(to).build();

        SearchTransactionCriteria criteria = SearchTransactionCriteria.builder()
                .accountId(1L).from(from).to(to).page(1).size(20).build();
        List<SearchTransactionProjection> list = List.of(new SearchTransactionProjection());
        TransactionHistoryResult expected = TransactionHistoryResult.builder()
                .accountId(1L).items(List.of()).build();

        given(mapper.toCriteria(query)).willReturn(criteria);
        given(loadTransactionPort.loadByAccountInRange(criteria)).willReturn(list);
        given(mapper.toResult(list)).willReturn(expected);

        // when
        TransactionHistoryResult result = transactionService.getByAccount(query);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        then(mapper).should(times(1)).toCriteria(query);
        then(loadTransactionPort).should(times(1)).loadByAccountInRange(criteria);
        then(mapper).should(times(1)).toResult(list);
    }

}
