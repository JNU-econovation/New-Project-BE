package com.econo_4factorial.newproject.base.exception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum BaseErrorType implements ErrorType {
    BASE_NOT_FOUND_EXCEPTION("BASE404_001", HttpStatus.NOT_FOUND, "거점을 찾을 수 없습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String Message;

    BaseErrorType(String errorCode, HttpStatus httpStatus, String Message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.Message = Message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return Message;
    }
}
