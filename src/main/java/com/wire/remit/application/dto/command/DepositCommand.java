package com.wire.remit.application.dto.command;

import lombok.Builder;

/**
 * 입금 command DTO
 * @param accountId 계좌 ID
 * @param amount 입금 금액
 */
@Builder
public record DepositCommand(
        Long accountId,
        long amount
) {
}
