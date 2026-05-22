package com.econo_4factorial.newproject.common.util.api;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResult<B> extends ResponseEntity<B> {
    public ApiResult(B body, HttpStatus status) {
        super(body, status);
    }

    public ApiResult(B body, HttpHeaders headers, HttpStatus status) {
        super(body, headers, status);
    }

    @Getter
    @AllArgsConstructor
    public static class SuccessBody<D> implements Serializable {
        private D data;
        private String status; //HttpStatus가 아닌 success를 의미하는 status임
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorBody implements Serializable {
        private String status; //HttpStatus가 아닌 fail를 의미하는 status임
        private String errorCode;
        private String message;
    }
}
