package com.econo_4factorial.newproject.auth.dto.kakao;

public record KakaoUserInfoDTO(
        Long kakaoId,
        String email,
        String name
) {
}
