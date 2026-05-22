package com.econo_4factorial.newproject.common.util.api;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@UtilityClass
public class ApiResponse {
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    public static ApiResult<ApiResult.SuccessBody<Void>> success(final HttpStatus status) {
        return new ApiResult<>(new ApiResult.SuccessBody<>(null, SUCCESS), status);
    }

    public static <D> ApiResult<ApiResult.SuccessBody<D>> success(final D data, final HttpStatus status) {
        return new ApiResult<>(new ApiResult.SuccessBody<>(data, SUCCESS), status);
    }

    public static ApiResult<ApiResult.SuccessBody<Void>> success(final HttpHeaders headers, final HttpStatus status) {
        return new ApiResult<>(new ApiResult.SuccessBody<Void>(null, SUCCESS), headers, status);
    }

    public static ApiResult<ApiResult.ErrorBody> fail(final String errorCode, final String message,
                                                      final HttpStatus status) {
        return new ApiResult<>(new ApiResult.ErrorBody(ERROR, errorCode, message), status);
    }

    public static ApiResult<ApiResult.ErrorBody> fail(final String errorCode, final String message,
                                                      final HttpHeaders headers, HttpStatus status) {
        return new ApiResult<>(new ApiResult.ErrorBody(ERROR, errorCode, message), headers, status);
    }
}
