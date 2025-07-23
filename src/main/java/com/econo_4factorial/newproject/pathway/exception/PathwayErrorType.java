package com.econo_4factorial.newproject.pathway.exception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum PathwayErrorType implements ErrorType {
    COORDINATES_PARSING_EXCEPTION("PATHWAY500_001", HttpStatus.INTERNAL_SERVER_ERROR, "(코스)경로의 위경도를 변환 중 에러가 발생하였습니다");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    PathwayErrorType(String errorCode, HttpStatus httpStatus, String message) {
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
