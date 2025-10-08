package com.wire.remit.adapter.out.persistence.repository;

import com.wire.remit.adapter.out.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {
}
