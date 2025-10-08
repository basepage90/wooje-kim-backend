package com.wire.remit.domain.account.service;

import com.wire.remit.domain.account.model.Money;
import com.wire.remit.domain.common.DomainException;
import com.wire.remit.domain.common.ErrorCode;

import java.math.BigDecimal;

public class DailyLimitPolicy {
    private static final Money WITHDRAW_LIMIT = new Money(BigDecimal.valueOf(1_000_000));
    private static final Money TRANSFER_LIMIT = new Money(BigDecimal.valueOf(3_000_000));

    public void checkWithdrawLimit(Money todaySum, Money request) {
        Money total = todaySum.add(request);
        if (total.isGreaterThan(WITHDRAW_LIMIT)) {
            throw new DomainException(ErrorCode.DAILY_WITHDRAW_LIMIT_EXCEEDED,
                    ErrorCode.DAILY_WITHDRAW_LIMIT_EXCEEDED.getMessage());
        }
    }

    public void checkTransferLimit(Money todaySum, Money request) {
        Money total = todaySum.add(request);
        if (total.isGreaterThan(TRANSFER_LIMIT)) {
            throw new DomainException(ErrorCode.DAILY_TRANSFER_LIMIT_EXCEEDED,
                    ErrorCode.DAILY_TRANSFER_LIMIT_EXCEEDED.getMessage());
        }
    }
}
