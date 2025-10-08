package com.econo_4factorial.newproject.auth.exception.InternalServerException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class InvalidVerificationCodeException extends BadRequestException {
    public InvalidVerificationCodeException() {
        super(AuthErrorType.INVALID_VERIFICATION_CODE_EXCEPTION);
    }
}
