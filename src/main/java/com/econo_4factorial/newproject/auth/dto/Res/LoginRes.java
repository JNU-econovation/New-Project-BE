package com.econo_4factorial.newproject.auth.dto.Res;

public record LoginRes(
        String accessToken,
        String refreshToken
) {
}
