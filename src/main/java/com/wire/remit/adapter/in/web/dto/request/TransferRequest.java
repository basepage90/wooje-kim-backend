package com.wire.remit.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "송금 request")
public record TransferRequest(
        @Schema(description = "송금 출발 계좌 ID", example = "1")
        @NotNull
        Long sourceAccountId,

        @Schema(description = "송금 대상 계좌 ID", example = "3")
        @NotNull
        Long targetAccountId,

        @Schema(description = "송금 금액", example = "500000", minimum = "1")
        @Min(1)
        long amount
) {
}