package com.wire.remit.domain.account.service;

import com.wire.remit.domain.account.model.Money;
import com.wire.remit.domain.common.DomainException;
import com.wire.remit.domain.common.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DailyLimitPolicy 단위 테스트")
class DailyLimitPolicyTest {

    private final DailyLimitPolicy policy = new DailyLimitPolicy();

    @Test
    @DisplayName("출금 합계가 한도 이내이면 예외가 발생하지 않는다")
    void checkWithdrawLimit_withinLimit() {
        Money todaySum = new Money(BigDecimal.valueOf(500_000));
        Money request = new Money(BigDecimal.valueOf(400_000));

        assertDoesNotThrow(() -> policy.checkWithdrawLimit(todaySum, request));
    }

    @Test
    @DisplayName("출금 합계가 한도를 초과하면 예외가 발생한다")
    void checkWithdrawLimit_exceedsLimit() {
        Money todaySum = new Money(BigDecimal.valueOf(800_000));
        Money request = new Money(BigDecimal.valueOf(300_000));

        DomainException exception = assertThrows(DomainException.class,
                () -> policy.checkWithdrawLimit(todaySum, request));

        assertEquals(ErrorCode.DAILY_WITHDRAW_LIMIT_EXCEEDED, exception.getCode());
        assertEquals(ErrorCode.DAILY_WITHDRAW_LIMIT_EXCEEDED.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("이체 합계가 한도 이내이면 예외가 발생하지 않는다")
    void checkTransferLimit_withinLimit() {
        Money todaySum = new Money(BigDecimal.valueOf(1_000_000));
        Money request = new Money(BigDecimal.valueOf(2_000_000));

        assertDoesNotThrow(() -> policy.checkTransferLimit(todaySum, request));
    }

    @Test
    @DisplayName("이체 합계가 한도를 초과하면 예외가 발생한다")
    void checkTransferLimit_exceedsLimit() {
        Money todaySum = new Money(BigDecimal.valueOf(2_500_000));
        Money request = new Money(BigDecimal.valueOf(600_000));

        DomainException exception = assertThrows(DomainException.class,
                () -> policy.checkTransferLimit(todaySum, request));

        assertEquals(ErrorCode.DAILY_TRANSFER_LIMIT_EXCEEDED, exception.getCode());
        assertEquals(ErrorCode.DAILY_TRANSFER_LIMIT_EXCEEDED.getMessage(), exception.getMessage());
    }
}
