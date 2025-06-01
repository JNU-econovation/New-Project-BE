package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class AuthException extends BadRequestException {
    public AuthException() {
        super(AuthErrorType.AUTH_EXCEPTION);
    }
}
