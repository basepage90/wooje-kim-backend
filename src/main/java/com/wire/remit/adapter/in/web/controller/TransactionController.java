package com.wire.remit.adapter.in.web.controller;

import com.wire.remit.adapter.in.web.dto.request.DepositRequest;
import com.wire.remit.adapter.in.web.dto.request.SearchTransactionRequest;
import com.wire.remit.adapter.in.web.dto.request.TransferRequest;
import com.wire.remit.adapter.in.web.dto.request.WithdrawRequest;
import com.wire.remit.adapter.in.web.dto.response.TransactionHistoryResponse;
import com.wire.remit.adapter.in.web.dto.response.TransactionResponse;
import com.wire.remit.adapter.in.web.mapper.TransactionWebMapper;
import com.wire.remit.application.port.in.TransactionUseCase;
import com.wire.remit.adapter.in.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "거래 (Transaction)", description = "입금 출금 및 이체 API")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionUseCase transactionUseCase;
    private final TransactionWebMapper mapper;

    @Operation(summary = "입금 요청", description = "특정 계좌에 금액을 입금합니다.")
    @PostMapping("/deposits")
    public ApiResponse<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        var result = transactionUseCase.deposit(mapper.toCommand(request));
        return ApiResponse.ok(mapper.toResponse(result));
    }

    @Operation(summary = "출금 요청", description = "특정 계좌에서 금액을 출금합니다.")
    @PostMapping("/withdrawals")
    public ApiResponse<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        var result = transactionUseCase.withdraw(mapper.toCommand(request));
        return ApiResponse.ok(mapper.toResponse(result));
    }

    @Operation(summary = "이체 요청", description = "한 계좌에서 다른 계좌로 금액을 이체합니다.")
    @PostMapping("/transfers")
    public ApiResponse<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        var result = transactionUseCase.transfer(mapper.toCommand(request));
        return ApiResponse.ok(mapper.toResponse(result));
    }

    @Operation(summary = "거래 내역 조회",description = "특정 계좌의 거래(입금/출금/이체) 내역을 조회합니다.")
    @GetMapping("/{accountId}")
    public ApiResponse<TransactionHistoryResponse> get(SearchTransactionRequest request) {
        var result = transactionUseCase.getByAccount(mapper.toCommand(request));
        return ApiResponse.ok(mapper.toResponse(result));
    }
}
