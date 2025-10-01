package com.econo_4factorial.newproject.user.dto;

import com.econo_4factorial.newproject.user.domain.User;

public record UserAlertSettingDTO(
        boolean eventAlert,
        boolean travelDeviationAlert,
        boolean accidentProneAreaAlert
) {
    public static UserAlertSettingDTO from(User user) {
        return new UserAlertSettingDTO(
                user.isEventAlert(),
                user.isTravelDeviationAlert(),
                user.isAccidentProneAreaAlert()
        );
    }
}
