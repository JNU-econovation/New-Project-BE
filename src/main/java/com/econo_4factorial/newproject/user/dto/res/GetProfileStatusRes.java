package com.econo_4factorial.newproject.user.dto.res;

import com.econo_4factorial.newproject.user.dto.UserInfoDTO;
import com.econo_4factorial.newproject.user.dto.UserStatusInfoDTO;

public record GetProfileStatusRes(
        UserStatusInfoDTO userStatusInfoDTO
) {
    public static GetProfileStatusRes from (UserStatusInfoDTO userStatusInfoDTO) {
        return new GetProfileStatusRes(userStatusInfoDTO);
    }
}