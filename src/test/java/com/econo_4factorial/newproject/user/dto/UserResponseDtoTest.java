package com.econo_4factorial.newproject.user.dto;

import com.econo_4factorial.newproject.user.domain.vo.UserAlert;
import com.econo_4factorial.newproject.user.dto.res.GetAlertSettingRes;
import com.econo_4factorial.newproject.user.dto.res.GetNicknameAvailabilityRes;
import com.econo_4factorial.newproject.user.dto.res.GetProfileRes;
import com.econo_4factorial.newproject.user.dto.res.GetProfileStatusRes;
import com.econo_4factorial.newproject.user.dto.res.GetRandomNicknameRes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseDtoTest {

    @Test
    void 알림설정_DTO를_생성한다() {
        UserAlert userAlert = new UserAlert();
        userAlert.updateAlerts(false, true, false);

        UserAlertSettingDTO userAlertSettingDTO = UserAlertSettingDTO.from(userAlert);

        assertThat(userAlertSettingDTO.eventAlert()).isFalse();
        assertThat(userAlertSettingDTO.travelDeviationAlert()).isTrue();
        assertThat(userAlertSettingDTO.accidentProneAreaAlert()).isFalse();
    }

    @Test
    void 프로필_상태정보_DTO를_보관한다() {
        ProfileStatusInfoDTO profileStatusInfoDTO = new ProfileStatusInfoDTO(true, false);

        assertThat(profileStatusInfoDTO.isBasicInfoSet()).isTrue();
        assertThat(profileStatusInfoDTO.isPersonalInfoSet()).isFalse();
    }

    @Test
    void 프로필_응답을_생성한다() {
        UserProfileDTO userProfileDTO = new UserProfileDTO("홍길동", "등산러", "01012345678", "test@example.com", 70L, 180L, null, null);

        GetProfileRes response = GetProfileRes.from(userProfileDTO);

        assertThat(response.userProfileDTO()).isEqualTo(userProfileDTO);
    }

    @Test
    void 프로필상태_응답을_생성한다() {
        ProfileStatusInfoDTO profileStatusInfoDTO = new ProfileStatusInfoDTO(true, true);

        GetProfileStatusRes response = GetProfileStatusRes.from(profileStatusInfoDTO);

        assertThat(response.profileStatusInfoDTO()).isEqualTo(profileStatusInfoDTO);
    }

    @Test
    void 알림설정_응답을_생성한다() {
        UserAlertSettingDTO userAlertSettingDTO = new UserAlertSettingDTO(true, false, true);

        GetAlertSettingRes response = GetAlertSettingRes.from(userAlertSettingDTO);

        assertThat(response.userAlertSetting()).isEqualTo(userAlertSettingDTO);
    }

    @Test
    void 랜덤_닉네임_응답을_생성한다() {
        GetRandomNicknameRes response = GetRandomNicknameRes.from("산타는호랑이");

        assertThat(response.nickname()).isEqualTo("산타는호랑이");
    }

    @Test
    void 닉네임_중복여부_응답을_생성한다() {
        GetNicknameAvailabilityRes response = GetNicknameAvailabilityRes.from(true);

        assertThat(response.isAvailable()).isTrue();
    }
}
