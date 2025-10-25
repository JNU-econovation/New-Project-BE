package com.econo_4factorial.newproject.travel.exception;

import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class TravelRecordNotFoundException extends BadRequestException {
    public TravelRecordNotFoundException() {
        super(TravelErrorType.TRAVEL_RECORD_NOT_FOUND_EXCEPTION);
    }
}
