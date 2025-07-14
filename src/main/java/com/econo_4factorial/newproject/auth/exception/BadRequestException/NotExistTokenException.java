package com.econo_4factorial.newproject.auth.exception.BadRequestException;

import com.econo_4factorial.newproject.auth.exception.AuthErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class NotExistTokenException extends BadRequestException {
    public NotExistTokenException() {
        super(AuthErrorType.NOT_EXIST_TOKEN_EXCEPTION);
    }
}
