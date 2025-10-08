package com.wire.remit.domain.account.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


@Getter
public class Money {
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        if (amount == null) throw new IllegalArgumentException("amount null");
        this.amount = amount.scale() > 0 ? amount.setScale(0, RoundingMode.HALF_UP) : amount;
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    public static Money of(long v) {
        return new Money(BigDecimal.valueOf(v));
    }

    public static Money of(BigDecimal balance) {
        return new Money(balance);
    }
}
