package com.econo_4factorial.newproject.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    private final ErrorType errorType;

    public BadRequestException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
