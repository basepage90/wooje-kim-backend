package com.wire.remit.domain.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {
    DEPOSIT("계좌입금"),
    WITHDRAW("계좌출금"),
    TRANSFER_OUT("이체출금"),
    TRANSFER_IN("이체입금"),
    FEE("수수료");

    private final String description;
}
