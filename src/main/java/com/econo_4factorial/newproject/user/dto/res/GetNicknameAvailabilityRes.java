package com.econo_4factorial.newproject.user.dto.res;

public record GetNicknameAvailabilityRes(
        boolean isAvailable
) {
    public static GetNicknameAvailabilityRes from(boolean isAvailable) {
        return new GetNicknameAvailabilityRes(isAvailable);
    }
}
