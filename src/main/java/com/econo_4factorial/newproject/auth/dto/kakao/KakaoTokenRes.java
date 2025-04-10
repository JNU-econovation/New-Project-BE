package com.econo_4factorial.newproject.auth.dto.kakao;

public record KakaoTokenRes(
        String tokenType,
        String accessToken,
        Integer expires_in,
        String refresh_token,
        Integer refresh_token_expires_in
) {
}
