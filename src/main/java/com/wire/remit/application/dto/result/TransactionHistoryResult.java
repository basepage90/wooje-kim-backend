package com.wire.remit.application.dto.result;

import com.wire.remit.domain.account.model.AccountStatus;
import com.wire.remit.domain.transaction.model.TransactionType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TransactionHistoryResult(
        Long accountId,
        AccountStatus status,
        List<TransactionItem> items
) {
    @Builder
    public record TransactionItem(
            Long transactionId,
            TransactionType type,
            long amount,
            LocalDateTime occurredAt
    ) {
    }
}
