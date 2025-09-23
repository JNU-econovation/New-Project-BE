package com.econo_4factorial.newproject.auth.exception.InternalServerException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.InternalServerException;

public class IncorrectVerificationCodeException extends InternalServerException {
    public IncorrectVerificationCodeException() {
        super(AuthErrorType.INCORRECT_VERIFICATION_CODE_EXCEPTION);
    }
}
