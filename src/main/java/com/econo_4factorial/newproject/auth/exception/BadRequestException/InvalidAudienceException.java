package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class InvalidAudienceException extends BadRequestException {
    public InvalidAudienceException() {
        super(AuthErrorType.INVALID_AUDIENCE_EXCEPTION);
    }
}
