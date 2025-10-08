package com.wire.remit.application.port.out;

import com.wire.remit.application.port.out.contract.SearchTransactionCriteria;
import com.wire.remit.application.port.out.contract.SearchTransactionProjection;
import com.wire.remit.domain.transaction.model.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface LoadTransactionPort {
    List<SearchTransactionProjection> loadByAccountInRange(SearchTransactionCriteria criteria);

    BigDecimal calculateTodayAmount(Long accountId, TransactionType type);

}
