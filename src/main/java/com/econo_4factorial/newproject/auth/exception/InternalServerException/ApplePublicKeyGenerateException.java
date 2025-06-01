package com.econo_4factorial.newproject.auth.exception.InternalServerException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.InternalServerException;

public class ApplePublicKeyGenerateException extends InternalServerException {
    public ApplePublicKeyGenerateException () {
        super(AuthErrorType.APPLE_PUBLIC_KEY_GENERATE_EXCEPTION);
    }
}
