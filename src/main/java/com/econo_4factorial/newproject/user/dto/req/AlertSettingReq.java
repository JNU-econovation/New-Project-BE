package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotNull;

public record AlertSettingReq(
        @NotNull(message = ValidationMessage.EVENT_ALERT_IS_REQUIRED)
        Boolean eventAlert,
        @NotNull(message = ValidationMessage.TRAVEL_DEVIATION_ALERT_IS_REQUIRED)
        Boolean travelDeviationAlert,
        @NotNull(message = ValidationMessage.ACCIDENT_PRONE_AREA_ALERT_IS_REQUIRED)
        Boolean accidentProneAreaAlert
) {
}
