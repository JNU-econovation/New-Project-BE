package com.econo_4factorial.newproject.auth.dto.kakao;

import com.econo_4factorial.newproject.user.dto.UserInfoDTO;

public record KaKaoUserInfoRes(
    KakaoAccount kakaoAccount
) {

    public record KakaoAccount(
            String email,
            String name,
            String phoneNumber
    ) {
        public KakaoAccount (String email, String name, String phoneNumber) {
            this.email = email;
            this.name = name;
            this.phoneNumber = formatPhoneNumber(phoneNumber);
        }

        private String formatPhoneNumber(String phoneNumber) {
            return phoneNumber.replace("+82 ", "0");
        }
    }

    public UserInfoDTO toUserInfoDTO() {
        return new UserInfoDTO(this.kakaoAccount.email, this.kakaoAccount.name, this.kakaoAccount.phoneNumber);
    }
}
