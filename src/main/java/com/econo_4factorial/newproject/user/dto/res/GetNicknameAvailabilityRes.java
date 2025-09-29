package com.econo_4factorial.newproject.user.dto.res;

public record GetNicknameAvailabilityRes(
        Boolean isAvailable
) {
    public static GetNicknameAvailabilityRes from(Boolean isAvailable) {
        return new GetNicknameAvailabilityRes(isAvailable);
    }
}
