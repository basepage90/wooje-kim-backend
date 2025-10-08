package com.wire.remit.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "출금 request")
public record WithdrawRequest(
        @Schema(description = "계좌 ID", example = "1")
        @NotNull
        Long accountId,

        @Schema(description = "출금 금액", example = "500000", minimum = "1")
        @Min(1)
        long amount
) {
}