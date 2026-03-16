package com.econo_4factorial.newproject.travel.exception;

import com.econo_4factorial.newproject.common.exception.BadRequestException;

public class TravelTrackingInfoNotFoundException extends BadRequestException {
    public TravelTrackingInfoNotFoundException() {
        super(TravelErrorType.TRAVEL_TRACKING_INFO_NOT_FOUND_EXCEPTION);
    }
}
