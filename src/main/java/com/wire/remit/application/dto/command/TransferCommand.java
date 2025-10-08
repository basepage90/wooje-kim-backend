package com.wire.remit.application.dto.command;

import lombok.Builder;

/**
 * 이체 command DTO
 * @param sourceAccountId 이체 출금계좌 ID
 * @param targetAccountId 이체 입금계좌 ID
 * @param amount 이체 금액
 */
@Builder
public record TransferCommand(
        Long sourceAccountId,
        Long targetAccountId,
        long amount
) {
}
