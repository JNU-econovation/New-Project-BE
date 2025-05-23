package com.econo_4factorial.newproject.common.exception;

public class InternalServerException extends RuntimeException{
    private final ErrorType errorType;

    public InternalServerException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
