package com.wire.remit.adapter.out.persistence.mapper;

import com.wire.remit.adapter.out.persistence.entity.AccountEntity;
import com.wire.remit.adapter.out.persistence.entity.TransactionEntity;
import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.Money;
import com.wire.remit.domain.transaction.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PersistenceMapper {

    public Account toDomain(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .ownerName(entity.getOwnerName())
                .accountName(entity.getAccountName())
                .balance(Money.of(entity.getBalance()))
                .status(entity.getStatus())
                .version(entity.getVersion())
                .build();
    }

    public AccountEntity toEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .ownerName(account.getOwnerName())
                .accountName(account.getAccountName())
                .balance(account.getBalance().getAmount())
                .status(account.getStatus())
                .version(account.getVersion())
                .build();
    }

    public Transaction toDomain(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .accountId(entity.getAccountEntity().getId())
                .type(entity.getType())
                .money(Money.of(entity.getAmount()))
                .occurredAt(entity.getOccurredAt())
                .build();
    }

    public TransactionEntity toEntity(Transaction transaction, AccountEntity accountEntity ) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .accountEntity(accountEntity)
                .type(transaction.getType())
                .amount(transaction.getMoney().getAmount())
                .occurredAt(LocalDateTime.now())
                .build();
    }
}
