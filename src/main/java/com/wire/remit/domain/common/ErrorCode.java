package com.wire.remit.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ACCOUNT_NOT_FOUND("계좌 없음"),
    DAILY_WITHDRAW_LIMIT_EXCEEDED("일일 출금 한도(1,000,000원) 초과"),
    DAILY_TRANSFER_LIMIT_EXCEEDED("일일 이체 한도(3,000,000원) 초과"),
    INSUFFICIENT_BALANCE("잔액 부족");

    private final String message;
}
