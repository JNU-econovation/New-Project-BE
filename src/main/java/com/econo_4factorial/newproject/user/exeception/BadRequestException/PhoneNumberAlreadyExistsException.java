package com.econo_4factorial.newproject.user.exeception.BadRequestException;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.user.exeception.UserErrorType;

public class PhoneNumberAlreadyExistsException extends BadRequestException {
    public PhoneNumberAlreadyExistsException() {
        super(UserErrorType.PHONE_NUMBER_ALREADY_EXISTS_EXCEPTION);
    }
}
