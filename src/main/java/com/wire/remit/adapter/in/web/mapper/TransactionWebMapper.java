package com.wire.remit.adapter.in.web.mapper;

import com.wire.remit.adapter.in.web.dto.request.DepositRequest;
import com.wire.remit.adapter.in.web.dto.request.SearchTransactionRequest;
import com.wire.remit.adapter.in.web.dto.request.TransferRequest;
import com.wire.remit.adapter.in.web.dto.request.WithdrawRequest;
import com.wire.remit.adapter.in.web.dto.response.TransactionHistoryResponse;
import com.wire.remit.adapter.in.web.dto.response.TransactionResponse;
import com.wire.remit.application.dto.command.DepositCommand;
import com.wire.remit.application.dto.command.TransferCommand;
import com.wire.remit.application.dto.command.WithdrawCommand;
import com.wire.remit.application.dto.query.TransactionQuery;
import com.wire.remit.application.dto.result.TransactionHistoryResult;
import com.wire.remit.application.dto.result.TransactionResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionWebMapper {

    public DepositCommand toCommand(DepositRequest request) {
        return DepositCommand.builder()
                .accountId(request.accountId())
                .amount(request.amount())
                .build();
    }

    public WithdrawCommand toCommand(WithdrawRequest request) {
        return WithdrawCommand.builder()
                .accountId(request.accountId())
                .amount(request.amount())
                .build();
    }

    public TransferCommand toCommand(TransferRequest request) {
        return TransferCommand.builder()
                .sourceAccountId(request.sourceAccountId())
                .targetAccountId(request.targetAccountId())
                .amount(request.amount())
                .build();
    }

    public TransactionQuery toCommand(SearchTransactionRequest request) {
        return TransactionQuery.builder()
                .accountId(request.accountId())
                .from(request.from())
                .to(request.to())
                .page(request.page())
                .size(request.size())
                .build();

    }

    public TransactionResponse toResponse(TransactionResult result) {
        return TransactionResponse.builder()
                .accountId(result.accountId())
                .type(result.type())
                .amount(result.amount())
                .occurredAt(result.occurredAt())
                .build();
    }

    public TransactionHistoryResponse toResponse(TransactionHistoryResult result) {
        if (result == null) {
            return new TransactionHistoryResponse(null, null, List.of());
        }

        List<TransactionHistoryResponse.TransactionItem> items = new ArrayList<>();

        for (TransactionHistoryResult.TransactionItem item : result.items()) {
            var response = TransactionHistoryResponse.TransactionItem.builder()
                    .transactionId(item.transactionId())
                    .type(item.type())
                    .amount(item.amount())
                    .occurredAt(item.occurredAt())
                    .build();
            items.add(response);
        }

        return new TransactionHistoryResponse(result.accountId(), result.status(), items);
    }

}
