package com.econo_4factorial.newproject.user.dto.res;

import com.econo_4factorial.newproject.user.dto.ProfileStatusInfoDTO;

public record GetProfileStatusRes(
        ProfileStatusInfoDTO profileStatusInfoDTO
) {
    public static GetProfileStatusRes from (ProfileStatusInfoDTO profileStatusInfoDTO) {
        return new GetProfileStatusRes(profileStatusInfoDTO);
    }
}