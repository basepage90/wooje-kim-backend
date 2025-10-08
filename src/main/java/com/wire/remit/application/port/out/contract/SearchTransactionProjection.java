package com.wire.remit.application.port.out.contract;

import com.wire.remit.domain.account.model.AccountStatus;
import com.wire.remit.domain.transaction.model.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SearchTransactionProjection {
    private Long accountId;
    private AccountStatus status;
    private Long transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime occurredAt;
}
