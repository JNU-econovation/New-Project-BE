package com.econo_4factorial.newproject.auth.exception.InternalServerException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.InternalServerException;

public class NotMatchedApplePublicKeyException extends InternalServerException {
    public NotMatchedApplePublicKeyException() {
        super(AuthErrorType.NOT_MATCHED_APPLE_PUBLIC_KEY_EXCEPTION);
    }
}
