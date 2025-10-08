package com.wire.remit.domain.transaction.model;

import com.wire.remit.domain.account.model.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long id;
    private Long accountId;
    private TransactionType type;
    private Money money;
    private LocalDateTime occurredAt;
}
