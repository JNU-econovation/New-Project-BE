package com.econo_4factorial.newproject.facility.exception.BadRequestException;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.facility.exception.FacilityErrorType;

public class FacilityNotFoundException extends BadRequestException {
    public FacilityNotFoundException() {
        super(FacilityErrorType.FACILITY_NOT_FOUND_EXCEPTION);
    }
}
