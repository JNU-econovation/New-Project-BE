package com.econo_4factorial.newproject.auth.exception.InternalServerException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class IncorrectVerificationCodeException extends BadRequestException {
    public IncorrectVerificationCodeException() {
        super(AuthErrorType.INCORRECT_VERIFICATION_CODE_EXCEPTION);
    }
}
