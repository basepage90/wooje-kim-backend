package com.wire.remit.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wire.remit.domain.account.model.AccountStatus;
import com.wire.remit.domain.transaction.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "거래 이력 조회 response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionHistoryResponse(
        @Schema(description = "계좌 ID", example = "1")
        Long accountId,

        @Schema(description = "계좌 상태", example = "ACTIVE")
        AccountStatus status,

        @Schema(description = "거래 내역 리스트")
        List<TransactionItem> items
) {
    @Builder
    @Schema(description = "거래 내역")
    public record TransactionItem(
            @Schema(description = "거래 ID", example = "1")
            Long transactionId,

            @Schema(description = "거래 유형", example = "DEPOSIT")
            TransactionType type,

            @Schema(description = "거래 금액", example = "1000000")
            long amount,

            @Schema(description = "거래 발생 일시", example = "2025-10-06 14:34:25")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime occurredAt
    ) {
    }
}
