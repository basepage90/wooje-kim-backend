package com.wire.remit.adapter.in.web.advice;

import com.wire.remit.adapter.in.web.ApiResponse;
import com.wire.remit.domain.common.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomain(DomainException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(exception.getMessage()));
    }
}
