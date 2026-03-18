package com.econo_4factorial.newproject.user.exception.BadRequestException;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.user.exception.UserErrorType;

public class EmailAlreadyExistsException extends BadRequestException {
    public EmailAlreadyExistsException() {
        super(UserErrorType.EMAIL_ALREADY_EXISTS_EXCEPTION);
    }
}
