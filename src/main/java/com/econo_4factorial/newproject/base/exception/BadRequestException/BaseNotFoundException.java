package com.econo_4factorial.newproject.base.exception.BadRequestException;

import com.econo_4factorial.newproject.base.exception.BaseErrorType;
import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class BaseNotFoundException extends BadRequestException {
    public BaseNotFoundException() {
        super(BaseErrorType.BASE_NOT_FOUND_EXCEPTION);
    }
}
