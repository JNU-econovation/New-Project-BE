package com.econo_4factorial.newproject.auth.exception;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum AuthErrorType implements ErrorType {
    AUTH_EXCEPTION ("Auth400_001", HttpStatus.FOUND, "로그인 과정 중 에러가 발생했습니다"),
    INVALID_TOKEN_EXCEPTION("Auth401_001", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN_EXCEPTION("Auth401_002", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다"),

    APPLE_TOKEN_HEADER_PARSING_EXCEPTION("Auth500_001", HttpStatus.INTERNAL_SERVER_ERROR, "애플 identityToken헤더 파싱 중 에러가 발생했습니다"),
    NOT_MATCHED_APPLE_PUBLIC_KEY_EXCEPTION("Auth500_002", HttpStatus.INTERNAL_SERVER_ERROR, "애플 identityToken의 서명(signature) 검증 중 매칭되는 공개키를 찾을 수 없습니다"),
    APPLE_PUBLIC_KEY_GENERATE_EXCEPTION ("Auth500_003", HttpStatus.INTERNAL_SERVER_ERROR, "애플 identityToken의 서명(signature) 검증에서 사용되는 공개키 생성 중 에러가 발생했습니다"),
    NOT_APPLE_ISSUER_EXCEPTION("Auth500_004", HttpStatus.INTERNAL_SERVER_ERROR, "애플 identityToken 검증 중 issuer가 애플이 아닙니다"),
    INVALID_AUDIENCE_EXCEPTION("Auth500_005", HttpStatus.INTERNAL_SERVER_ERROR, "애플 identityToken 검증 중 audience가 client_id가 아닙니다");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    AuthErrorType(String errorCode, HttpStatus httpStatus, String message) {
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
