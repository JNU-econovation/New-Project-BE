package com.econo_4factorial.newproject.common.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorType implements ErrorType{

    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("COMMON400_001", HttpStatus.BAD_REQUEST, "RequestDTO 유효성 검사 미통과"),
    ILLEGAL_ARGUMENT_EXCEPTION("COMMON400_002", HttpStatus.BAD_REQUEST, "Illegal argument exception 발생 "),
    MISSING_PATH_VARIABLE_EXCEPTION("COMMON400_003", HttpStatus.BAD_REQUEST, "경로 변수(PathVariable)가 누락됐습니다."),
    MISSING_REQUEST_PARAM_EXCEPTION("COMMON400_004", HttpStatus.BAD_REQUEST, "쿼리 스트링이 누락됐습니다.");
  
    UN_EXPECTED_EXCEPTION("Common500_001", HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 에러가 발생했습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    CommonErrorType(String errorCode, HttpStatus httpStatus, String message) {
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
