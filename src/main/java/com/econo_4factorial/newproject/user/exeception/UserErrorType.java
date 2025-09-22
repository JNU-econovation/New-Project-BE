package com.econo_4factorial.newproject.user.exeception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum UserErrorType implements ErrorType {
    USER_NOT_FOUND_EXCEPTION ("USER400_001", HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다"),
    PHONE_NUMBER_ALREADY_EXISTS_EXCEPTION("USER400_002", HttpStatus.NOT_FOUND, "이미 존재하는 전화번호입니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    UserErrorType(String errorCode, HttpStatus httpStatus, String message) {
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
