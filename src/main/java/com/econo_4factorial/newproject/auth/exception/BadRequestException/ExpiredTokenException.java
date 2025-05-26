package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class ExpiredTokenException extends BadRequestException {
    public ExpiredTokenException() {
        super(AuthErrorType.EXPIRED_TOKEN_EXCEPTION);
    }
}
