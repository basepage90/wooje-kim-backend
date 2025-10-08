package com.wire.remit.application.mapper;

import com.wire.remit.application.dto.command.RegisterAccountCommand;
import com.wire.remit.application.dto.result.AccountResult;
import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.AccountStatus;
import com.wire.remit.domain.account.model.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountAppMapper {

    public Account toRegisterDomain(RegisterAccountCommand command) {
        return Account.builder()
                .ownerName(command.ownerName())
                .accountName(command.accountName())
                .balance(Money.of(BigDecimal.ZERO))
                .status(AccountStatus.ACTIVE)
                .build();
    }

    public AccountResult toAccountResult(Account account) {
        return AccountResult.builder()
                .id(account.getId())
                .ownerName(account.getOwnerName())
                .accountName(account.getAccountName())
                .balance(account.getBalance().getAmount().longValue())
                .status(account.getStatus())
                .build();
    }
}
