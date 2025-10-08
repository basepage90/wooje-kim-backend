package com.wire.remit.adapter.in.web.dto.response;

import com.wire.remit.domain.account.model.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "계좌 등록 response")
@Builder
public record AccountResponse(
        @Schema(description = "계좌 ID", example = "1")
        Long id,

        @Schema(description = "예금주 이름", example = "김큰손")
        String ownerName,

        @Schema(description = "계좌 잔액", example = "100000")
        long balance,

        @Schema(description = "계좌 상태", example = "ACTIVE")
        AccountStatus status
) {
}