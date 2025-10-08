package com.econo_4factorial.newproject.course.exception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum CourseErrorType implements ErrorType {
    COURSE_NOT_FOUND_EXCEPTION ("COURSE404_001", HttpStatus.NOT_FOUND, "코스를 찾을 수 없습니다"),

    BOOKMARK_NOT_FOUND_EXCEPTION ("BOOKMARK404_001", HttpStatus.NOT_FOUND, "코스 북마크를 찾을 수 없습니다"),

    CLOSEST_COORDINATE_NOT_FOUND_EXCEPTION("TRAVEL500_001", HttpStatus.INTERNAL_SERVER_ERROR, "유저 좌표와 가장 가까운 pathway좌표가 없습니다");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    CourseErrorType(String errorCode, HttpStatus httpStatus, String message) {
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
