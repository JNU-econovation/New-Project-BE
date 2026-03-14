package com.econo_4factorial.newproject.user.dto;

import com.econo_4factorial.newproject.user.domain.BloodType;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.domain.vo.PhysicalInfo;

public record UserProfileDTO(
        String name,
        String nickname,
        String phoneNumber,
        String email,
        Long weight,
        Long height,
        BloodType bloodType,
        String etc
) {
    public static UserProfileDTO from(User user) {
        PhysicalInfo physicalInfo = user.getPhysicalInfo();
        return new UserProfileDTO(
                user.getUserInfo().getName(),
                user.getNickname(),
                user.getUserInfo().getPhoneNumber(),
                user.getUserInfo().getEmail(),
                physicalInfo != null ? physicalInfo.getWeight() : null,
                physicalInfo != null ? physicalInfo.getHeight() : null,
                physicalInfo != null ? physicalInfo.getBloodType() : null,
                physicalInfo != null ? physicalInfo.getEtc() : null
        );
    }
}
