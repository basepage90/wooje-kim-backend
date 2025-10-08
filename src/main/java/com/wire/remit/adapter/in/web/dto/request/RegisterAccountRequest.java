package com.wire.remit.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "계좌 등록 request")
public record RegisterAccountRequest(

        @Schema(description = "예금주 이름", example = "김큰손", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String ownerName,

        @Schema(description = "계좌명", example = "월급통장")
        @Size(max = 30)
        String accountName
) {

}
