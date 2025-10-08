package com.wire.remit.adapter.out.persistence.queryRepository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wire.remit.adapter.out.persistence.PagePolicy;
import com.wire.remit.adapter.out.persistence.entity.QAccountEntity;
import com.wire.remit.adapter.out.persistence.entity.QTransactionEntity;
import com.wire.remit.adapter.out.persistence.queryRepository.TransactionQueryRepository;
import com.wire.remit.application.port.out.contract.SearchTransactionCriteria;
import com.wire.remit.application.port.out.contract.SearchTransactionProjection;
import com.wire.remit.domain.transaction.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class TransactionQueryRepositoryImpl implements TransactionQueryRepository {

    private final JPAQueryFactory query;
    private final PagePolicy pagePolicy;

    private final QTransactionEntity transaction = QTransactionEntity.transactionEntity;
    private final QAccountEntity account = QAccountEntity.accountEntity;


    @Override
    public BigDecimal calculateTodayAmount(Long accountId, TransactionType type) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        BigDecimal result = query
                .select(transaction.amount.sum())
                .from(transaction)
                .where(
                        transaction.accountEntity.id.eq(accountId),
                        transaction.type.eq(type),
                        transaction.occurredAt.between(startOfDay, endOfDay)
                )
                .fetchOne();

        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public List<SearchTransactionProjection> findByCriteria(SearchTransactionCriteria criteria) {
        long offSet = pagePolicy.offset(criteria.page(), criteria.size());
        int size = pagePolicy.getSize(criteria.size());

        return query
                .select(Projections.fields(SearchTransactionProjection.class,
                        account.id.as("accountId"),
                        account.status,
                        transaction.id.as("transactionId"),
                        transaction.accountEntity,
                        transaction.amount,
                        transaction.type,
                        transaction.occurredAt
                ))
                .from(transaction)
                .join(transaction.accountEntity, account)
                .where(
                        eqAccountId(criteria.accountId()),
                        betweenDate(criteria.from(), criteria.to())
                )
                .orderBy(transaction.occurredAt.desc())
                .offset(offSet)
                .limit(size)
                .fetch();
    }


    private BooleanExpression eqAccountId(Long accountId) {
        return accountId != null ? transaction.accountEntity.id.eq(accountId) : null;
    }

    private BooleanExpression betweenDate(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null) return transaction.occurredAt.between(from, to);
        if (from != null) return transaction.occurredAt.goe(from);
        if (to != null) return transaction.occurredAt.loe(to);
        return null;
    }

}
