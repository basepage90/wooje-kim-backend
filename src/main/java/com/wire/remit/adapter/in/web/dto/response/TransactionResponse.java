package com.wire.remit.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wire.remit.domain.transaction.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "거래 내역 response")
@Builder
public record TransactionResponse(
        @Schema(description = "계좌 ID", example = "1")
        Long accountId,

        @Schema(description = "거래 유형", example = "DEPOSIT")
        TransactionType type,

        @Schema(description = "거래 금액", example = "500000")
        long amount,

        @Schema(description = "거래 발생 일시", example = "2025-10-06 14:34:25")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime occurredAt
) {
}