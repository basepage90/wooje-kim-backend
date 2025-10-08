package com.wire.remit.application.service;

import com.wire.remit.application.dto.command.RegisterAccountCommand;
import com.wire.remit.application.dto.result.AccountResult;
import com.wire.remit.application.mapper.AccountAppMapper;
import com.wire.remit.application.port.in.AccountUseCase;
import com.wire.remit.application.port.out.LoadAccountPort;
import com.wire.remit.application.port.out.SaveAccountPort;
import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.common.DomainException;
import com.wire.remit.domain.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final AccountAppMapper mapper;

    /**
     * 계좌 등록
     */
    @Override
    public AccountResult register(RegisterAccountCommand command) {
        Account account = mapper.toRegisterDomain(command);
        Account saved = saveAccountPort.save(account);
        return mapper.toAccountResult(saved);
    }

    /**
     * 계좌 삭제
     */
    @Override
    public void delete(Long accountId) {
        Account account = loadAccountPort.findById(accountId).orElseThrow(() -> new DomainException(ErrorCode.ACCOUNT_NOT_FOUND));
        account.close();
        saveAccountPort.save(account);
    }
}
