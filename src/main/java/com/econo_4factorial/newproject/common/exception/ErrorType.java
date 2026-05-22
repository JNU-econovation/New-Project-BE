package com.econo_4factorial.newproject.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorType {
    String getErrorCode();

    HttpStatus getHttpStatus();

    String getMessage();
}
