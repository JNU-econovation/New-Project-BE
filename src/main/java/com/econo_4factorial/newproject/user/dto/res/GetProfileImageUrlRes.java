package com.econo_4factorial.newproject.user.dto.res;

import com.econo_4factorial.newproject.user.dto.ProfileImageUrlDTO;

public record GetProfileImageUrlRes(
        ProfileImageUrlDTO profileImageUrlDTO
) {
    public static GetProfileImageUrlRes from(ProfileImageUrlDTO profileImageUrlDTO) {
        return new GetProfileImageUrlRes(profileImageUrlDTO);
    }
}