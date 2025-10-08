package com.wire.remit.adapter.out.portimpl;

import com.wire.remit.adapter.out.persistence.entity.AccountEntity;
import com.wire.remit.adapter.out.persistence.repository.AccountJpaRepository;
import com.wire.remit.application.port.out.LoadTransactionPort;
import com.wire.remit.application.port.out.SaveTransactionPort;
import com.wire.remit.adapter.out.persistence.entity.TransactionEntity;
import com.wire.remit.adapter.out.persistence.mapper.PersistenceMapper;
import com.wire.remit.adapter.out.persistence.repository.TransactionJpaRepository;
import com.wire.remit.adapter.out.persistence.queryRepository.TransactionQueryRepository;
import com.wire.remit.application.port.out.contract.SearchTransactionCriteria;
import com.wire.remit.application.port.out.contract.SearchTransactionProjection;
import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.AccountStatus;
import com.wire.remit.domain.transaction.model.Transaction;
import com.wire.remit.domain.transaction.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements LoadTransactionPort, SaveTransactionPort {

    private final TransactionJpaRepository jpa;
    private final AccountJpaRepository accountJpa;
    private final TransactionQueryRepository queryRepository;
    private final PersistenceMapper mapper;


    @Override
    public BigDecimal calculateTodayAmount(Long accountId, TransactionType type) {
        return queryRepository.calculateTodayAmount(accountId, type);
    }

    @Override
    public List<SearchTransactionProjection> loadByAccountInRange(SearchTransactionCriteria criteria) {
        return queryRepository.findByCriteria(criteria);
    }

    @Override
    public Transaction save(Transaction transaction) {
        AccountEntity accountEntity = accountJpa.getReferenceById(transaction.getAccountId());
        TransactionEntity transactionEntity = mapper.toEntity(transaction, accountEntity);
        TransactionEntity saved = jpa.save(transactionEntity);
        return mapper.toDomain(saved);
    }
}
