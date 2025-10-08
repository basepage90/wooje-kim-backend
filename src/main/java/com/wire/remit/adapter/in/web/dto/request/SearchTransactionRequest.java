package com.wire.remit.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "거래 이력 조회 request DTO")
public record SearchTransactionRequest(
        @Schema(description = "계좌 ID", example = "1")
        Long accountId,

        @Schema(description = "검색 시작일시", type = "string", example = "2024-10-06 00:00:00")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime from,

        @Schema(description = "검색 종료일시", type = "string", example = "2025-10-30 23:59:59")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime to,

        @Schema(description = "페이지 번호", example = "1")
        int page,

        @Schema(description = "페이지 크기", example = "20")
        int size
) {
}
