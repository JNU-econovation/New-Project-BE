package com.econo_4factorial.newproject.user.dto.res;

import com.econo_4factorial.newproject.user.dto.UserProfileDTO;

public record GetProfileRes(
        UserProfileDTO userProfileDTO
) {
    public static GetProfileRes from(UserProfileDTO userProfileDTO) {
        return new GetProfileRes(userProfileDTO);
    }
}
