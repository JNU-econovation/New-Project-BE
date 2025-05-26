package com.econo_4factorial.newproject.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KaKaoUserInfoRes(
    Long id,
    @JsonProperty("kakao_account")
    KakaoAccount kakaoAccount
) {

    public record KakaoAccount(
            String email,
            String name
    ) {
        public KakaoAccount (String email, String name) {
            this.email = email;
            this.name = name;
        }

    }

    public KakaoUserInfoDTO toKaKaoUserInfoDTO() {
        return new KakaoUserInfoDTO(this.id, this.kakaoAccount.email, this.kakaoAccount.name);
    }
}
