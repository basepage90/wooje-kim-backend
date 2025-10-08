package com.wire.remit.adapter.out.persistence.repository;

import com.wire.remit.adapter.out.persistence.entity.AccountEntity;
import com.wire.remit.domain.account.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByIdAndStatus(Long id, AccountStatus status);
}
