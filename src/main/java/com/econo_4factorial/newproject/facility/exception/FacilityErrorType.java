package com.econo_4factorial.newproject.facility.exception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum FacilityErrorType implements ErrorType {
    FACILITY_NOT_FOUND_EXCEPTION("FACILITY404_001", HttpStatus.NOT_FOUND, "시설을 찾을 수 없습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    FacilityErrorType(String errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getErrorCode () {
        return errorCode;
    }

    @Override
    public HttpStatus getHttpStatus () {
        return httpStatus;
    }

    @Override
    public String getMessage () {
        return message;
    }
}
