package com.wire.remit.application.port.in;

import com.wire.remit.application.dto.command.RegisterAccountCommand;
import com.wire.remit.application.dto.result.AccountResult;

public interface AccountUseCase {
    AccountResult register(RegisterAccountCommand command);

    void delete(Long accountId);
}
