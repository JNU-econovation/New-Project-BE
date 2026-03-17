package com.econo_4factorial.newproject.user.service;

import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import com.econo_4factorial.newproject.user.domain.BloodType;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.dto.ProfileStatusInfoDTO;
import com.econo_4factorial.newproject.user.dto.UserAlertSettingDTO;
import com.econo_4factorial.newproject.user.dto.UserProfileDTO;
import com.econo_4factorial.newproject.user.dto.req.AddBasicInformationReq;
import com.econo_4factorial.newproject.user.dto.req.AddPersonalInformationReq;
import com.econo_4factorial.newproject.user.dto.req.AlertSettingReq;
import com.econo_4factorial.newproject.user.dto.req.ProfileSettingReq;
import com.econo_4factorial.newproject.user.exeception.BadRequestException.EmailAlreadyExistsException;
import com.econo_4factorial.newproject.user.exeception.BadRequestException.PhoneNumberAlreadyExistsException;
import com.econo_4factorial.newproject.user.exeception.BadRequestException.UserNotFoundException;
import com.econo_4factorial.newproject.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void 유저ID로_유저를_조회한다() {
        User user = createUser(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        User foundUser = userService.findUserByIdOrThrow(1L);

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void 유저가_없으면_예외가_발생한다() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserByIdOrThrow(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 카카오ID로_기존유저를_찾으면_그대로_반환한다() {
        KakaoUserInfoDTO userInfoDTO = new KakaoUserInfoDTO(1L, "test@example.com", "홍길동");
        User user = createUser(1L);
        given(userRepository.findByKakaoId(1L)).willReturn(Optional.of(user));

        User result = userService.findOrCreateUserByKakaoUserInfo(userInfoDTO);

        assertThat(result).isEqualTo(user);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void 애플_sub로_유저가_없으면_새로_저장한다() {
        AppleUserInfoDTO userInfoDTO = new AppleUserInfoDTO("apple-sub", "홍길동", "test@example.com");
        User savedUser = User.appleUserBuilder().appleSub("apple-sub").email("test@example.com").name("홍길동").build();
        given(userRepository.findByAppleSub("apple-sub")).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        User result = userService.findOrCreateUserByAppleUserInfo(userInfoDTO);

        assertThat(result).isEqualTo(savedUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void 프로필_등록상태를_반환한다() {
        User user = createUser(1L);
        user.registerBasicInformation("등산러", "010-1234-5678", "test@example.com");
        user.registerPersonalInformation("홍길동", 70L, 175L, BloodType.A);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        ProfileStatusInfoDTO profileStatusInfoDTO = userService.isProfileSet(1L);

        assertThat(profileStatusInfoDTO.isBasicInfoSet()).isTrue();
        assertThat(profileStatusInfoDTO.isPersonalInfoSet()).isTrue();
    }

    @Test
    void 닉네임이_중복되지_않으면_true를_반환한다() {
        given(userRepository.existsByNickname("등산러")).willReturn(false);

        boolean isUnique = userService.isNicknameUnique("등산러");

        assertThat(isUnique).isTrue();
    }

    @Test
    void 전화번호가_이미_존재하면_예외가_발생한다() {
        given(userRepository.existsByUserInfoPhoneNumber("010-1234-5678")).willReturn(true);

        assertThatThrownBy(() -> userService.validateExistPhoneNumber("010-1234-5678"))
                .isInstanceOf(PhoneNumberAlreadyExistsException.class);
    }

    @Test
    void 이메일이_이미_존재하면_예외가_발생한다() {
        given(userRepository.existsByUserInfoEmail("test@example.com")).willReturn(true);

        assertThatThrownBy(() -> userService.validateExistEmail("test@example.com"))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void 기본정보를_등록한다() {
        User user = createUser(1L);
        AddBasicInformationReq request = new AddBasicInformationReq("등산러", "010-1234-5678", "test@example.com");
        given(userRepository.existsByUserInfoEmail("test@example.com")).willReturn(false);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.registerBasicInformation(1L, request);

        assertThat(user.getNickname()).isEqualTo("등산러");
        assertThat(user.getUserInfo().getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(user.getUserInfo().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void 개인정보를_등록한다() {
        User user = createUser(1L);
        AddPersonalInformationReq request = new AddPersonalInformationReq("홍길동", 70L, 175L, "AB");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.registerPersonalInformation(1L, request);

        assertThat(user.getUserInfo().getName()).isEqualTo("홍길동");
        assertThat(user.getPhysicalInfo().getBloodType()).isEqualTo(BloodType.AB);
    }

    @Test
    void 유저_알림설정을_조회한다() {
        User user = createUser(1L);
        user.updateUserAlert(false, true, false);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserAlertSettingDTO userAlertSettingDTO = userService.getUserAlertSetting(1L);

        assertThat(userAlertSettingDTO.eventAlert()).isFalse();
        assertThat(userAlertSettingDTO.travelDeviationAlert()).isTrue();
        assertThat(userAlertSettingDTO.accidentProneAreaAlert()).isFalse();
    }

    @Test
    void 유저_알림설정을_수정한다() {
        User user = createUser(1L);
        AlertSettingReq request = new AlertSettingReq(true, null, false);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.updateAlertSetting(1L, request);

        assertThat(user.getUserAlert().isEventAlert()).isTrue();
        assertThat(user.getUserAlert().isTravelDeviationAlert()).isFalse();
        assertThat(user.getUserAlert().isAccidentProneAreaAlert()).isFalse();
    }

    @Test
    void 프로필_파일명을_수정한다() {
        User user = createUser(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.updateUserProfileFileName(1L, "profile/1/test.jpeg");

        assertThat(user.getProfileFileName()).isEqualTo("profile/1/test.jpeg");
    }

    @Test
    void 유저_프로필을_조회한다() {
        User user = createUser(1L);
        user.updateUserProfile("홍길동", "test@example.com", "등산러", "010-1234-5678", 70L, 175L, BloodType.O, "메모");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        UserProfileDTO userProfileDTO = userService.getUserProfile(1L);

        assertThat(userProfileDTO.nickname()).isEqualTo("등산러");
        assertThat(userProfileDTO.bloodType()).isEqualTo(BloodType.O);
    }

    @Test
    void 유저_프로필을_수정한다() {
        User user = createUser(1L);
        ProfileSettingReq request = new ProfileSettingReq("홍길동", "test@example.com", "등산러", "010-1234-5678", 70L, 175L, "B", "메모");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.updateUserProfile(1L, request);

        assertThat(user.getNickname()).isEqualTo("등산러");
        assertThat(user.getPhysicalInfo().getBloodType()).isEqualTo(BloodType.B);
        assertThat(user.getPhysicalInfo().getEtc()).isEqualTo("메모");
    }

    @Test
    void 유저_프로필_파일명을_조회한다() {
        User user = createUser(1L);
        user.updateProfileFile("profile/1/test.jpeg");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        String fileName = userService.getUserProfileFileName(1L);

        assertThat(fileName).isEqualTo("profile/1/test.jpeg");
    }

    @Test
    void 유저_프로필_파일명을_삭제한다() {
        User user = createUser(1L);
        user.updateProfileFile("profile/1/test.jpeg");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userService.deleteUserProfileFileName(1L);

        assertThat(user.getProfileFileName()).isNull();
    }

    @Test
    void 중복되지_않는_전화번호는_예외없이_통과한다() {
        given(userRepository.existsByUserInfoPhoneNumber("010-1234-5678")).willReturn(false);

        assertThatCode(() -> userService.validateExistPhoneNumber("010-1234-5678"))
                .doesNotThrowAnyException();
    }

    private User createUser(Long userId) {
        User user = User.kakaoUserBuilder()
                .kakaoId(userId)
                .email("before@example.com")
                .name("임꺽정")
                .build();
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }
}
