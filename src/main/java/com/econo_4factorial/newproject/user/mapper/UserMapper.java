package com.econo_4factorial.newproject.user.mapper;

import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import com.econo_4factorial.newproject.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class UserMapper {
    public static User toEntity(KakaoUserInfoDTO userInfoDTO) {
        return User.kakaoUserBuilder()
                .kakaoId(userInfoDTO.kakaoId())
                .email(userInfoDTO.email())
                .name(userInfoDTO.name())
                .build();
    }

    public static User toEntity(AppleUserInfoDTO userInfoDTO) {
        return User.appleUserBuilder()
                .appleSub(userInfoDTO.appleSub())
                .email(userInfoDTO.email())
                .name(userInfoDTO.name())
                .build();
    }
}
