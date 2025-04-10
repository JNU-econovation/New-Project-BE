package com.econo_4factorial.newproject.user.mapper;

import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.dto.UserInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class UserMapper {
    public static User toEntity (UserInfoDTO userInfoDTO) {
        return User.builder()
                .email(userInfoDTO.email())
                .name(userInfoDTO.name())
                .phoneNumber(userInfoDTO.phoneNumber())
                .build();
    }
}
