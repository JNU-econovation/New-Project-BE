package com.econo_4factorial.newproject.user.dto;

import com.econo_4factorial.newproject.user.domain.BloodType;
import com.econo_4factorial.newproject.user.domain.User;

public record UserProfileDTO(
        String name,
        String nickname,
        String phoneNumber,
        //image_url
        String email,
        Long weight,
        Long height,
        BloodType bloodType,
        String etc
) {
    public static UserProfileDTO from(User user) {
        return new UserProfileDTO(
                user.getUserInfo().getName(),
                user.getNickname(),
                user.getUserInfo().getPhoneNumber(),
                user.getUserInfo().getEmail(),
                user.getPhysicalInfo().getWeight(),
                user.getPhysicalInfo().getHeight(),
                user.getPhysicalInfo().getBloodType(),
                user.getEtc()
        );
    }
}
