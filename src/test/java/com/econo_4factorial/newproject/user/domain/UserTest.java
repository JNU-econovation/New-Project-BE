package com.econo_4factorial.newproject.user.domain;

import com.econo_4factorial.newproject.user.domain.vo.PhysicalInfo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void 카카오_유저를_생성한다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("test@example.com")
                .name("홍길동")
                .build();

        assertThat(user.getKakaoId()).isEqualTo(1L);
        assertThat(user.getAppleSub()).isNull();
        assertThat(user.getUserInfo().getEmail()).isEqualTo("test@example.com");
        assertThat(user.getUserInfo().getName()).isEqualTo("홍길동");
    }

    @Test
    void 애플_유저를_생성한다() {
        User user = User.appleUserBuilder()
                .appleSub("apple-sub")
                .email("test@example.com")
                .name("홍길동")
                .build();

        assertThat(user.getAppleSub()).isEqualTo("apple-sub");
        assertThat(user.getKakaoId()).isNull();
    }

    @Test
    void 기본정보를_등록한다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("before@example.com")
                .name("홍길동")
                .build();

        user.registerBasicInformation("등산러", "010-1234-5678", "after@example.com");

        assertThat(user.getNickname()).isEqualTo("등산러");
        assertThat(user.getUserInfo().getPhoneNumber()).isEqualTo("010-1234-5678");
        assertThat(user.getUserInfo().getEmail()).isEqualTo("after@example.com");
        assertThat(user.isBasicInfoSet()).isTrue();
    }

    @Test
    void 개인_정보를_등록한다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("test@example.com")
                .name("홍길동")
                .build();

        user.registerPersonalInformation("임꺽정", 70L, 175L, BloodType.B);

        PhysicalInfo physicalInfo = user.getPhysicalInfo();
        assertThat(user.getUserInfo().getName()).isEqualTo("임꺽정");
        assertThat(physicalInfo.getWeight()).isEqualTo(70L);
        assertThat(physicalInfo.getHeight()).isEqualTo(175L);
        assertThat(physicalInfo.getBloodType()).isEqualTo(BloodType.B);
        assertThat(user.isPersonalInfoSet()).isTrue();
    }

    @Test
    void 알림설정을_수정한다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("test@example.com")
                .name("홍길동")
                .build();

        user.updateUserAlert(false, false, true);

        assertThat(user.getUserAlert().isEventAlert()).isFalse();
        assertThat(user.getUserAlert().isTravelDeviationAlert()).isFalse();
        assertThat(user.getUserAlert().isAccidentProneAreaAlert()).isTrue();
    }

    @Test
    void 프로필정보를_수정하면서_신체정보가_없으면_새로_생성한다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("test@example.com")
                .name("홍길동")
                .build();

        user.updateUserProfile("임꺽정", "new@example.com", "등산러", "010-9999-8888", 80L, 180L, BloodType.O, "메모");

        assertThat(user.getUserInfo().getName()).isEqualTo("임꺽정");
        assertThat(user.getUserInfo().getEmail()).isEqualTo("new@example.com");
        assertThat(user.getNickname()).isEqualTo("등산러");
        assertThat(user.getUserInfo().getPhoneNumber()).isEqualTo("010-9999-8888");
        assertThat(user.getPhysicalInfo().getWeight()).isEqualTo(80L);
        assertThat(user.getPhysicalInfo().getEtc()).isEqualTo("메모");
    }

    @Test
    void 프로필정보를_수정하면서_기존_신체정보를_덮어쓴다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("test@example.com")
                .name("홍길동")
                .build();
        user.registerPersonalInformation("홍길동", 70L, 175L, BloodType.A);

        user.updateUserProfile("임꺽정", "new@example.com", "등산러", "010-9999-8888", 80L, 180L, BloodType.O, "메모");

        assertThat(user.getPhysicalInfo().getWeight()).isEqualTo(80L);
        assertThat(user.getPhysicalInfo().getHeight()).isEqualTo(180L);
        assertThat(user.getPhysicalInfo().getBloodType()).isEqualTo(BloodType.O);
        assertThat(user.getPhysicalInfo().getEtc()).isEqualTo("메모");
    }

    @Test
    void 프로필_이미지명을_수정하고_삭제한다() {
        User user = User.kakaoUserBuilder()
                .kakaoId(1L)
                .email("test@example.com")
                .name("홍길동")
                .build();

        user.updateProfileFile("profile.png");
        assertThat(user.getProfileFileName()).isEqualTo("profile.png");

        user.deleteProfileImage();

        assertThat(user.getProfileFileName()).isNull();
    }
}
