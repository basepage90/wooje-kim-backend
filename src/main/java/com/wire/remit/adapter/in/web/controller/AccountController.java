package com.wire.remit.adapter.in.web.controller;

import com.wire.remit.adapter.in.web.ApiResponse;
import com.wire.remit.adapter.in.web.dto.request.RegisterAccountRequest;
import com.wire.remit.adapter.in.web.dto.response.AccountResponse;
import com.wire.remit.adapter.in.web.mapper.AccountWebMapper;
import com.wire.remit.application.port.in.AccountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "계좌 관리 (Account)", description = "계좌 등록 및 삭제 API")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;
    private final AccountWebMapper mapper;

    @Operation(summary = "계좌 등록", description = "새로운 송금 계좌를 등록합니다.")
    @PostMapping
    public ApiResponse<AccountResponse> register(@Valid @RequestBody RegisterAccountRequest request) {
        var result = accountUseCase.register(mapper.toCommand(request));
        return ApiResponse.ok(mapper.toResponse(result));
    }

    @Operation(summary = "계좌 삭제", description = "ID를 통해 특정 계좌 정보를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        accountUseCase.delete(id);
        return ApiResponse.ok();
    }
}
