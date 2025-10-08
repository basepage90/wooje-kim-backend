package com.wire.remit.domain.account.service;

import com.wire.remit.domain.account.model.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FeePolicy {
    private static final BigDecimal FEE_RATE = BigDecimal.valueOf(0.01); // 1%

    public Money transferFee(Money money) {
        BigDecimal fee = money.getAmount().multiply(FEE_RATE).setScale(0, RoundingMode.HALF_UP);
        return new Money(fee);
    }
}
