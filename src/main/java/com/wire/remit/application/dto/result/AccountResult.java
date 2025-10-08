package com.wire.remit.application.dto.result;

import com.wire.remit.domain.account.model.AccountStatus;
import lombok.Builder;

/**
 * 계좌등록 결과
 * @param id 계좌 ID
 * @param ownerName 예금주 이름
 * @param accountName 계좌이름
 * @param balance 잔액
 * @param status 상태
 */
@Builder
public record AccountResult(
        Long id,
        String ownerName,
        String accountName,
        long balance,
        AccountStatus status
) {
}
