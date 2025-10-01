package com.econo_4factorial.newproject.user.dto.res;

import com.econo_4factorial.newproject.user.dto.UserAlertSettingDTO;

public record GetAlertSettingRes(
        UserAlertSettingDTO userAlertSetting
) {
    public static GetAlertSettingRes from (UserAlertSettingDTO userAlertSetting) {
        return new GetAlertSettingRes(userAlertSetting);
    }
}
