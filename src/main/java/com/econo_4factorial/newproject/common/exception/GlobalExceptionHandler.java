package com.econo_4factorial.newproject.common.exception;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.AuthException;
import com.econo_4factorial.newproject.common.util.HttpHeadersGenerator;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String ERROR = "error";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ErrorType errorType = CommonErrorType.METHOD_ARGUMENT_NOT_VALID_EXCEPTION;

        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().getFirst().getDefaultMessage();

        return new ResponseEntity<>(new ApiResult.ErrorBody(ERROR, errorType.getErrorCode(), errorMessage),
                errorType.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ErrorType errorType = CommonErrorType.MISSING_PATH_VARIABLE_EXCEPTION;
        return new ResponseEntity<>(new ApiResult.ErrorBody(ERROR, errorType.getErrorCode(), errorType.getMessage()),
                errorType.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ErrorType errorType = CommonErrorType.MISSING_REQUEST_PARAM_EXCEPTION;
        return new ResponseEntity<>(new ApiResult.ErrorBody(ERROR, errorType.getErrorCode(), errorType.getMessage()),
                errorType.getHttpStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResult<ApiResult.ErrorBody> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorType errorType = CommonErrorType.ILLEGAL_ARGUMENT_EXCEPTION;
        return ApiResponse.fail(
                errorType.getErrorCode(),
                errorType.getMessage(),
                errorType.getHttpStatus()
        );
    }

    @ExceptionHandler(AuthException.class)
    public ApiResult<ApiResult.ErrorBody> handleAuthException(AuthException e) {
        ErrorType errorType = e.getErrorType();
        log.error(errorType.getErrorCode());

        HttpHeaders headers = HttpHeadersGenerator.setLocation(RedirectUriBuilder.buildLoginFailUri());
        return ApiResponse.fail(
                errorType.getErrorCode(),
                errorType.getMessage(),
                headers,
                HttpStatus.FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ApiResult<ApiResult.ErrorBody> handleBadRequest(BadRequestException ex) {
        ErrorType errorType = ex.getErrorType();
        log.error(errorType.getErrorCode());

        return ApiResponse.fail(
                errorType.getErrorCode(),
                errorType.getMessage(),
                errorType.getHttpStatus()
        );
    }

    @ExceptionHandler(InternalServerException.class)
    public ApiResult<ApiResult.ErrorBody> handleInternalServerException(InternalServerException ex) {
        ErrorType errorType = ex.getErrorType();
        log.error(errorType.getErrorCode());

        return ApiResponse.fail(
                errorType.getErrorCode(),
                errorType.getMessage(),
                errorType.getHttpStatus()
        );
    }
}
