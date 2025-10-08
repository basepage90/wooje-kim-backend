package com.wire.remit.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Schema(description = "입금 request")
public record DepositRequest(
        @Schema(description = "계좌 ID", example = "1")
        @NotNull Long accountId,

        @Schema(description = "입금 금액", example = "1000000", minimum = "1")
        @Min(1) long amount
) {
}
