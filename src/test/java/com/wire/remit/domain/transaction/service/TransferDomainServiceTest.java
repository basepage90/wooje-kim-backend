package com.wire.remit.domain.transaction.service;

import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TransferDomainService 단위 테스트")
class TransferDomainServiceTest {

    private final TransferDomainService service = new TransferDomainService();

    @Test
    @DisplayName("송금 시 출금계좌는 금액+수수료만큼 차감되고, 입금계좌는 금액만큼 증가한다")
    void transfer_success() {
        var sourceBalance = Money.of(1_000_000L);
        var targetBalance = Money.of(300_000L);

        // given
        Account source = Account.builder().id(1L).balance(sourceBalance).build();
        Account target = Account.builder().id(2L).balance(targetBalance).build();

        Money amount = Money.of(500_000L);
        Money fee = Money.of(5_000L);

        // when
        service.transfer(source, target, amount, fee);

        // then
        // 출금 계좌: 1,000,000 - (500,000 + 5,000) = 495,000
        assertEquals(BigDecimal.valueOf(495_000), source.getBalance().getAmount());
        // 입금 계좌: 300,00 + 500,000 = 800,000
        assertEquals(BigDecimal.valueOf(800_000), target.getBalance().getAmount());
    }

    @Test
    @DisplayName("송금 금액이 0이면 양쪽 계좌 잔액 변화가 없다")
    void transfer_zeroAmount() {
        var sourceBalance = Money.of(1_000_000L);
        var targetBalance = Money.of(300_000L);

        // given
        Account source = Account.builder().id(1L).balance(sourceBalance).build();
        Account target = Account.builder().id(2L).balance(targetBalance).build();

        Money amount = new Money(BigDecimal.ZERO);
        Money fee = new Money(BigDecimal.ZERO);

        // when
        service.transfer(source, target, amount, fee);

        // then
        assertEquals(BigDecimal.valueOf(1_000_000L), source.getBalance().getAmount());
        assertEquals(BigDecimal.valueOf(300_000L), target.getBalance().getAmount());
    }
}
