package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class InvalidTokenException extends BadRequestException {
    public InvalidTokenException() {
        super(AuthErrorType.INVALID_TOKEN_EXCEPTION);
    }
}
