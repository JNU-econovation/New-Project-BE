package com.econo_4factorial.newproject.mountain.exception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum MountainErrorType implements ErrorType {
    MOUNTAIN_NOT_FOUND_EXCEPTION("MOUNTAIN400_001", HttpStatus.NOT_FOUND, "산을 찾을 수 없습니다");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    MountainErrorType(String errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
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
        return message;
    }
}
