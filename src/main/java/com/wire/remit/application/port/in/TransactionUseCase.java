package com.wire.remit.application.port.in;

import com.wire.remit.application.dto.command.DepositCommand;
import com.wire.remit.application.dto.command.TransferCommand;
import com.wire.remit.application.dto.command.WithdrawCommand;
import com.wire.remit.application.dto.query.TransactionQuery;
import com.wire.remit.application.dto.result.TransactionHistoryResult;
import com.wire.remit.application.dto.result.TransactionResult;

public interface TransactionUseCase {
    TransactionResult deposit(DepositCommand command);

    TransactionResult withdraw(WithdrawCommand command);

    TransactionResult transfer(TransferCommand command);

    TransactionHistoryResult getByAccount(TransactionQuery query);

}
