package com.wire.remit.adapter.out.persistence.queryRepository;

import com.wire.remit.application.port.out.contract.SearchTransactionCriteria;
import com.wire.remit.application.port.out.contract.SearchTransactionProjection;
import com.wire.remit.domain.transaction.model.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionQueryRepository {
    List<SearchTransactionProjection> findByCriteria(SearchTransactionCriteria criteria);

    BigDecimal calculateTodayAmount(Long accountId, TransactionType type);
}
