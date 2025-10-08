package com.wire.remit.application.port.out;

import com.wire.remit.domain.account.model.Account;

import java.util.Optional;

public interface LoadAccountPort {
    Optional<Account> findById(Long accountId);
}
