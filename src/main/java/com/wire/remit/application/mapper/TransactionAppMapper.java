package com.wire.remit.application.mapper;

import com.wire.remit.application.dto.query.TransactionQuery;
import com.wire.remit.application.dto.result.TransactionHistoryResult;
import com.wire.remit.application.dto.result.TransactionResult;
import com.wire.remit.application.port.out.contract.SearchTransactionCriteria;
import com.wire.remit.application.port.out.contract.SearchTransactionProjection;
import com.wire.remit.domain.account.model.AccountStatus;
import com.wire.remit.domain.account.model.Money;
import com.wire.remit.domain.transaction.model.Transaction;
import com.wire.remit.domain.transaction.model.TransactionType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionAppMapper {

    public Transaction toDomain(Long accountId, Long amount, TransactionType type) {
        return Transaction.builder()
                .accountId(accountId)
                .type(type)
                .money(Money.of(amount))
                .occurredAt(LocalDateTime.now())
                .build();
    }

    public TransactionResult toResult(Transaction transaction) {
        return TransactionResult.builder()
                .accountId(transaction.getAccountId())
                .type(transaction.getType())
                .amount(transaction.getMoney().getAmount().longValue())
                .occurredAt(transaction.getOccurredAt())
                .build();
    }

    public TransactionResult toResultWithFee(Transaction transaction, Long fee) {
        return TransactionResult.builder()
                .accountId(transaction.getAccountId())
                .type(transaction.getType())
                .amount(transaction.getMoney().getAmount().longValue())
                .fee(fee)
                .occurredAt(transaction.getOccurredAt())
                .build();
    }

    public SearchTransactionCriteria toCriteria(TransactionQuery query) {
        return SearchTransactionCriteria.builder()
                .accountId(query.accountId())
                .to(query.to())
                .from(query.from())
                .page(query.page())
                .size(query.size())
                .build();
    }

    public TransactionHistoryResult toResult(List<SearchTransactionProjection> projections) {
        if (projections.isEmpty()) {
            return TransactionHistoryResult.builder()
                    .accountId(null)
                    .status(null)
                    .items(List.of())
                    .build();
        }

        Long accountId = projections.getFirst().getAccountId();
        AccountStatus status = projections.getFirst().getStatus();
        List<TransactionHistoryResult.TransactionItem> items = new ArrayList<>();

        for (SearchTransactionProjection p : projections) {
            var result = TransactionHistoryResult.TransactionItem.builder()
                    .transactionId(p.getTransactionId())
                    .type(p.getType())
                    .amount(p.getAmount().longValue())
                    .occurredAt(p.getOccurredAt())
                    .build();
            items.add(result);
        }

        return TransactionHistoryResult.builder()
                .accountId(accountId)
                .status(status)
                .items(items)
                .build();
    }
}
