package com.wire.remit.application.port.out;

import com.wire.remit.domain.account.model.Account;

public interface SaveAccountPort {
    Account save(Account account);
}
