package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class LoggedOutTokenException extends BadRequestException {
    public LoggedOutTokenException() {
        super(AuthErrorType.LOGGED_OUT_TOKEN_EXCEPTION);
    }
}
