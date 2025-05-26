package com.econo_4factorial.newproject.auth.exception.InternalServerException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.InternalServerException;

public class NotAppleIssuerException extends InternalServerException {
    public NotAppleIssuerException() {
        super(AuthErrorType.NOT_APPLE_ISSUER_EXCEPTION);
    }
}
