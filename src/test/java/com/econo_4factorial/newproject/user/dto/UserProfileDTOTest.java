package com.econo_4factorial.newproject.user.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.user.domain.BloodType;
import com.econo_4factorial.newproject.user.domain.User;
import org.junit.jupiter.api.Test;

class UserProfileDTOTest {

    @Test
    void 유저를_프로필_DTO로_변환한다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("test@example.com")
                .name("홍길동")
                .build();
        user.updateUserProfile("임꺽정", "new@example.com", "등산러", "010-1234-5678", 70L, 175L, BloodType.AB, "메모");

        UserProfileDTO userProfileDTO = UserProfileDTO.from(user);

        assertThat(userProfileDTO.name()).isEqualTo("임꺽정");
        assertThat(userProfileDTO.nickname()).isEqualTo("등산러");
        assertThat(userProfileDTO.phoneNumber()).isEqualTo("010-1234-5678");
        assertThat(userProfileDTO.email()).isEqualTo("new@example.com");
        assertThat(userProfileDTO.weight()).isEqualTo(70L);
        assertThat(userProfileDTO.height()).isEqualTo(175L);
        assertThat(userProfileDTO.bloodType()).isEqualTo(BloodType.AB);
        assertThat(userProfileDTO.etc()).isEqualTo("메모");
    }

    @Test
    void 신체정보가_없으면_null_값으로_프로필_DTO를_생성한다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("test@example.com")
                .name("홍길동")
                .build();
        user.registerBasicInformation("등산러", "010-1234-5678", "test@example.com");

        UserProfileDTO userProfileDTO = UserProfileDTO.from(user);

        assertThat(userProfileDTO.weight()).isNull();
        assertThat(userProfileDTO.height()).isNull();
        assertThat(userProfileDTO.bloodType()).isNull();
        assertThat(userProfileDTO.etc()).isNull();
    }
}
