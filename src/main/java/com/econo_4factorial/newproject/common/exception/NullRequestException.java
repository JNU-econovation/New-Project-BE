package com.econo_4factorial.newproject.common.exception;

public class NullRequestException extends BadRequestException {
    public NullRequestException() {
        super(CommonErrorType.NULL_REQUEST_EXCEPTION);
    }
}
