package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class InvalidRefreshTokenException extends BadRequestException {
    public InvalidRefreshTokenException() {
        super(AuthErrorType.INVALID_REFRESH_TOKEN_EXCEPTION);
    }
}
