package com.econo_4factorial.newproject.user.dto;

import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.domain.vo.UserAlert;

public record UserAlertSettingDTO(
        boolean eventAlert,
        boolean travelDeviationAlert,
        boolean accidentProneAreaAlert
) {
    public static UserAlertSettingDTO from(UserAlert userAlert) {
        return new UserAlertSettingDTO(
                userAlert.isEventAlert(),
                userAlert.isTravelDeviationAlert(),
                userAlert.isAccidentProneAreaAlert()
        );
    }
}
