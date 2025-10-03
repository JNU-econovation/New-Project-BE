package com.econo_4factorial.newproject.user.dto.req;

public record ProfileSettingReq(
        String name,
        String email,
        String nickname,
        String phoneNumber,
        Long weight,
        Long height,
        String bloodType,
        String etc
) {
}
