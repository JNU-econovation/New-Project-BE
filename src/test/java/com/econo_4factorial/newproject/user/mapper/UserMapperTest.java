package com.econo_4factorial.newproject.user.mapper;

import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import com.econo_4factorial.newproject.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void 카카오_유저정보를_엔티티로_변환한다() {
        KakaoUserInfoDTO userInfoDTO = new KakaoUserInfoDTO(1L, "test@example.com", "홍길동");

        User user = UserMapper.toEntity(userInfoDTO);

        assertThat(user.getKakaoId()).isEqualTo(1L);
        assertThat(user.getAppleSub()).isNull();
        assertThat(user.getUserInfo().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void 애플_유저정보를_엔티티로_변환한다() {
        AppleUserInfoDTO userInfoDTO = new AppleUserInfoDTO("apple-sub", "홍길동", "test@example.com");

        User user = UserMapper.toEntity(userInfoDTO);

        assertThat(user.getAppleSub()).isEqualTo("apple-sub");
        assertThat(user.getKakaoId()).isNull();
        assertThat(user.getUserInfo().getName()).isEqualTo("홍길동");
    }
}
