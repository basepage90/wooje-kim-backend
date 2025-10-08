package com.wire.remit.application.port.out;

import com.wire.remit.domain.transaction.model.Transaction;

public interface SaveTransactionPort {
    Transaction save(Transaction tx);
}
