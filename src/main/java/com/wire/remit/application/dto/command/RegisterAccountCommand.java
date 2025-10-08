package com.wire.remit.application.dto.command;

import lombok.Builder;

import java.util.Objects;

/**
 * 계좌 등록 command DTO.
 * @param ownerName          예금주
 * @param accountName        계좌명
 */
@Builder
public record RegisterAccountCommand(
        String ownerName,
        String accountName
) {
    public RegisterAccountCommand {
        Objects.requireNonNull(ownerName, "ownerName must not be null");
    }
}
