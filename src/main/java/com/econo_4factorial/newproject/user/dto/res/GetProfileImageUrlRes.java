package com.econo_4factorial.newproject.user.dto.res;

import com.econo_4factorial.newproject.user.dto.ProfileImageUrlDTO;

public record GetProfileImageUrlRes(
        String profileImageUrl
) {
    public static GetProfileImageUrlRes from(ProfileImageUrlDTO profileImageUrlDTO) {
        return new GetProfileImageUrlRes(profileImageUrlDTO.profileImageUrl());
    }
}