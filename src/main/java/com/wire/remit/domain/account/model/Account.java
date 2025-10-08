package com.wire.remit.domain.account.model;

import com.wire.remit.domain.common.DomainException;
import com.wire.remit.domain.common.ErrorCode;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private String ownerName;
    private String accountName;
    private Money balance;
    private AccountStatus status;
    private Long version;

    public void deposit(Money amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        Money after = this.balance.subtract(amount);
        if (after.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        this.balance = after;
    }

    public void close() { this.status = AccountStatus.CLOSED; }
}

