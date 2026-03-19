package com.econo_4factorial.newproject.travel.exception;

import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class NotExistStatusException extends BadRequestException {
    public NotExistStatusException() {
        super(TravelErrorType.NOT_EXIST_STATUS_EXCEPTION);
    }
}
