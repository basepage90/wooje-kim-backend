package com.wire.remit.application.port.out.contract;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchTransactionCriteria(
        Long accountId,
        LocalDateTime to,
        LocalDateTime from,
        int page,
        int size
) {
}
