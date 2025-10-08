package com.wire.remit.application.dto.command;

import lombok.Builder;

/**
 * 출금 command DTO
 * @param accountId 출금계좌 ID
 * @param amount 출금 금액
 */
@Builder
public record WithdrawCommand(
        Long accountId,
        long amount
) {
}
