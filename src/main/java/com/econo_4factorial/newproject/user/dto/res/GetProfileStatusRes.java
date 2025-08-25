package com.econo_4factorial.newproject.user.dto.res;

public record GetProfileStatusRes(
        Boolean isComplete
) {
    public static GetProfileStatusRes from (Boolean bool) {
        return new GetProfileStatusRes(bool);
    }
}
