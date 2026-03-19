package com.econo_4factorial.newproject.user.domain.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserInfoTest {

    @Test
    void 기본정보가_모두_채워지면_기본정보_등록완료로_판단한다() {
        UserInfo userInfo = new UserInfo("test@example.com", "홍길동");

        userInfo.updatePhoneNumber("010-1234-5678");

        assertThat(userInfo.isBasicInfoSet()).isTrue();
    }

    @Test
    void 이메일을_정상적으로_수정한다() {
        UserInfo userInfo = new UserInfo("before@example.com", "홍길동");

        userInfo.updateEmail("after@example.com");

        assertThat(userInfo.getEmail()).isEqualTo("after@example.com");
    }

    @Test
    void 이름을_정상적으로_수정한다() {
        UserInfo userInfo = new UserInfo("test@example.com", "홍길동");

        userInfo.updateName("임꺽정");

        assertThat(userInfo.getName()).isEqualTo("임꺽정");
    }

    @Test
    void 하이픈이_포함된_전화번호를_정상적으로_수정한다() {
        UserInfo userInfo = new UserInfo("test@example.com", "홍길동");

        userInfo.updatePhoneNumber("010-1234-5678");

        assertThat(userInfo.getPhoneNumber()).isEqualTo("010-1234-5678");
    }

    @Test
    void 하이픈이_없는_전화번호면_예외가_발생한다() {
        UserInfo userInfo = new UserInfo("test@example.com", "홍길동");

        assertThatThrownBy(() -> userInfo.updatePhoneNumber("01012345678"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("전화번호값은 올바르지 않은 형태입니다");
    }

    @Test
    void 잘못된_이메일_형식이면_예외가_발생한다() {
        UserInfo userInfo = new UserInfo("test@example.com", "홍길동");

        assertThatThrownBy(() -> userInfo.updateEmail("invalid-email"))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessage("이메일값은 올바르지 않은 형태입니다");
    }

    @Test
    void 잘못된_이름_형식이면_예외가_발생한다() {
        UserInfo userInfo = new UserInfo("test@example.com", "홍길동");

        assertThatThrownBy(() -> userInfo.updateName("홍길동1"))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessage("이름값은 올바르지 않은 형태입니다");
    }

    @Test
    void 잘못된_전화번호_접두사면_예외가_발생한다() {
        UserInfo userInfo = new UserInfo("test@example.com", "홍길동");

        assertThatThrownBy(() -> userInfo.updatePhoneNumber("011-1234-5678"))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessage("전화번호값은 올바르지 않은 형태입니다");
    }
}
