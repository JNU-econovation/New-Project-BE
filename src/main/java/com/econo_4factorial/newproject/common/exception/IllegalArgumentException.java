package com.econo_4factorial.newproject.common.exception;

public class IllegalArgumentException extends BadRequestException {
    public IllegalArgumentException() {
        super(CommonErrorType.ILLEGAL_ARGUMENT_EXCEPTION);
    }
}
