package com.wire.remit.application.dto.result;

import com.wire.remit.domain.transaction.model.TransactionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionResult(
        Long accountId,
        TransactionType type,
        long amount,
        long fee,
        LocalDateTime occurredAt
) {
}
