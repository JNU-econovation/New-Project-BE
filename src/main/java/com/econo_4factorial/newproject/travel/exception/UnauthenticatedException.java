package com.econo_4factorial.newproject.travel.exception;

import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class UnauthenticatedException extends BadRequestException {
    public UnauthenticatedException() {
        super(TravelErrorType.UNAUTHENTICATED_EXCEPTION);
    }
}
