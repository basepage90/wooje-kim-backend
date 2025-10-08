package com.wire.remit.domain.common;


import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final ErrorCode code;

    public DomainException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public DomainException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
