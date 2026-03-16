package com.econo_4factorial.newproject.travel.exception;

import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class NotAllowedEventForStatusException extends BadRequestException {
    public NotAllowedEventForStatusException() {
        super(TravelErrorType.NOT_ALLOWED_EVENT_FOR_STATUS_EXCEPTION);
    }
}
