package com.econo_4factorial.newproject.travel.exception;

import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class NotExistEventException extends BadRequestException {
    public NotExistEventException() {
        super(TravelErrorType.NOT_EXIST_EVENT_EXCEPTION);
    }
}
