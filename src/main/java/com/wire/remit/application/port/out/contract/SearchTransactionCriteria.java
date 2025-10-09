package com.wire.remit.application.port.out.contract;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SearchTransactionCriteria(
        Long accountId,
        LocalDateTime from,
        LocalDateTime to,
        int page,
        int size
) {
}
