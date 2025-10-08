package com.wire.remit.adapter.in.web.mapper;

import com.wire.remit.adapter.in.web.dto.request.RegisterAccountRequest;
import com.wire.remit.adapter.in.web.dto.response.AccountResponse;
import com.wire.remit.application.dto.command.RegisterAccountCommand;
import com.wire.remit.application.dto.result.AccountResult;
import org.springframework.stereotype.Component;

@Component
public class AccountWebMapper {

    public RegisterAccountCommand toCommand(RegisterAccountRequest request) {
        return RegisterAccountCommand.builder()
                .ownerName(request.ownerName())
                .accountName(request.accountName())
                .build();
    }

    public AccountResponse toResponse(AccountResult result) {
        return AccountResponse.builder()
                .id(result.id())
                .ownerName(result.ownerName())
                .balance(result.balance())
                .status(result.status())
                .build();
    }
}
