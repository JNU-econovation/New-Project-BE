package com.econo_4factorial.newproject.travel.exception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;

public enum TravelErrorType implements ErrorType {
    NOT_ALLOWED_EVENT_FOR_STATUS_EXCEPTION("TRAVEL400_001", HttpStatus.BAD_REQUEST, "현재 유저 상태에서는 허락되지 않은 이벤트입니다"),
    UNAUTHENTICATED_EXCEPTION("TRAVEL400_002", HttpStatus.BAD_REQUEST, "사용자 인증이 되지 않은 상태에서는 이벤트 처리가 불가능합니다"),

    NOT_EXIST_EVENT_EXCEPTION("TRAVEL404_001", HttpStatus.NOT_FOUND, "존재하지 않는 이벤트입니다"),
    TRAVEL_TRACKING_INFO_NOT_FOUND_EXCEPTION("TRAVEL404_002", HttpStatus.NOT_FOUND, "유저의 등산 상태 정보를 찾을 수 없습니다");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    TravelErrorType(String errorCode, HttpStatus httpStatus, String message) {
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