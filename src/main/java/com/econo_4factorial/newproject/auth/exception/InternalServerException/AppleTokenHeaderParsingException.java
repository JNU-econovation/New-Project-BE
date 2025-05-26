package com.econo_4factorial.newproject.auth.exception.InternalServerException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.InternalServerException;

public class AppleTokenHeaderParsingException extends InternalServerException {
    public AppleTokenHeaderParsingException() {
        super(AuthErrorType.APPLE_TOKEN_HEADER_PARSING_EXCEPTION);
    }
}
