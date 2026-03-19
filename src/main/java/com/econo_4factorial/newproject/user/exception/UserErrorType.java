package com.econo_4factorial.newproject.user.exception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum UserErrorType implements ErrorType {
    USER_NOT_FOUND_EXCEPTION ("USER400_001", HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다"),
    PHONE_NUMBER_ALREADY_EXISTS_EXCEPTION("USER400_002", HttpStatus.BAD_REQUEST, "이미 존재하는 전화번호입니다."),
    EMAIL_ALREADY_EXISTS_EXCEPTION("USER400_003", HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    INVALID_BLOOD_TYPE_EXCEPTION("USER400_004", HttpStatus.BAD_REQUEST, "혈액형은 A, B, O, AB만 허용됩니다.");

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
