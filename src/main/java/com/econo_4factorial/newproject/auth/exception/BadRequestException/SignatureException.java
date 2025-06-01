package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class SignatureException extends BadRequestException {
    public SignatureException() {
        super(AuthErrorType.SIGNATURE_EXCEPTION);
    }
}
