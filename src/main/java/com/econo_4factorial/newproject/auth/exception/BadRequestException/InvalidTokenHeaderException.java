package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class InvalidTokenHeaderException extends BadRequestException {
    public InvalidTokenHeaderException() {
        super(AuthErrorType.INVALID_TOKEN_HEADER);
    }
}
