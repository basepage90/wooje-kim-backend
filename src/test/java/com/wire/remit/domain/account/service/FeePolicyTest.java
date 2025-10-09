package com.wire.remit.domain.account.service;

import com.wire.remit.domain.account.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FeePolicy 단위 테스트")
class FeePolicyTest {

    private final FeePolicy policy = new FeePolicy();

    @Test
    @DisplayName("송금 금액의 1%를 수수료로 계산한다.")
    void transferFee_exactPercentage() {
        // given
        Money money = new Money(BigDecimal.valueOf(100_000));

        // when
        Money fee = policy.transferFee(money);

        // then
        assertEquals(BigDecimal.valueOf(1_000), fee.getAmount());
    }

    @Test
    @DisplayName("송금 금액의 1% 계산 결과가 소수점일 경우 반올림한다.")
    void transferFee_roundingUp() {
        // given
        Money money = new Money(BigDecimal.valueOf(555_555));

        // when
        Money fee = policy.transferFee(money);

        // then
        // 555,555 * 0.01 = 5,555.55 → 반올림 → 5,556
        assertEquals(BigDecimal.valueOf(5_556), fee.getAmount());
    }

    @Test
    @DisplayName("송금 금액이 0이면 수수료가 0이다")
    void transferFee_zeroAmount() {
        // given
        Money money = new Money(BigDecimal.ZERO);

        // when
        Money fee = policy.transferFee(money);

        // then
        assertEquals(BigDecimal.ZERO, fee.getAmount());
    }
}
