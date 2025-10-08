package com.wire.remit.adapter.out.portimpl;

import com.wire.remit.adapter.out.persistence.entity.AccountEntity;
import com.wire.remit.adapter.out.persistence.mapper.PersistenceMapper;
import com.wire.remit.adapter.out.persistence.repository.AccountJpaRepository;
import com.wire.remit.application.port.out.LoadAccountPort;
import com.wire.remit.application.port.out.SaveAccountPort;
import com.wire.remit.domain.account.model.Account;
import com.wire.remit.domain.account.model.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, SaveAccountPort {

    private final AccountJpaRepository jpa;
    private final PersistenceMapper mapper;


    @Override
    public Optional<Account> findById(Long accountId) {
        return jpa.findByIdAndStatus(accountId, AccountStatus.ACTIVE).map(mapper::toDomain);
    }

    @Override
    public Account save(Account account) {
        AccountEntity saved = jpa.save(mapper.toEntity(account));
        return mapper.toDomain(saved);
    }
}
