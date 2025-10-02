package com.econo_4factorial.newproject.user.exeception.BadRequestException;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.user.exeception.UserErrorType;

public class InvalidBloodTypeException extends BadRequestException {
    public InvalidBloodTypeException() {
        super(UserErrorType.INVALID_BLOOD_TYPE_EXCEPTION);
    }
}
