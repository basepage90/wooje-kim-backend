package com.wire.remit.application.dto.query;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 거래내역 조회 query DTO
 * @param accountId 계좌 ID
 * @param to 거래일시 시작
 * @param from 거래일시 끝
 * @param page 페이지 번호
 * @param size 페이지 사이즈
 */
@Builder
public record TransactionQuery(
        Long accountId,
        LocalDateTime to,
        LocalDateTime from,
        int page,
        int size
) {
}
