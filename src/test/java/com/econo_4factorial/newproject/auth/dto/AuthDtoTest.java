package com.econo_4factorial.newproject.auth.dto;

import com.econo_4factorial.newproject.auth.dto.Req.AppleLoginReq;
import com.econo_4factorial.newproject.auth.dto.Req.FullName;
import com.econo_4factorial.newproject.auth.dto.Res.AppleLoginRes;
import com.econo_4factorial.newproject.auth.dto.Res.SendSmsRes;
import com.econo_4factorial.newproject.auth.dto.Res.VerifySmsRes;
import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.dto.kakao.KaKaoUserInfoRes;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthDtoTest {

    @Test
    void 애플_로그인_요청을_애플_유저정보_DTO로_변환한다() {
        AppleLoginReq request = new AppleLoginReq("identity-token", "test@example.com", new FullName("홍", "길동"));

        AppleUserInfoDTO appleUserInfoDTO = request.toAppleUserInfoDTO("apple-sub");

        assertThat(appleUserInfoDTO.appleSub()).isEqualTo("apple-sub");
        assertThat(appleUserInfoDTO.name()).isEqualTo("홍길동");
        assertThat(appleUserInfoDTO.email()).isEqualTo("test@example.com");
    }

    @Test
    void 애플_로그인_요청에서_이름정보가_없으면_null_이름으로_변환한다() {
        AppleLoginReq request = new AppleLoginReq("identity-token", "test@example.com", new FullName("", " "));

        AppleUserInfoDTO appleUserInfoDTO = request.toAppleUserInfoDTO("apple-sub");

        assertThat(appleUserInfoDTO.name()).isNull();
    }

    @Test
    void 애플_로그인_응답을_토큰정보로부터_생성한다() {
        AuthToken authToken = AuthToken.of("access-token", "refresh-token", 3600L);

        AppleLoginRes response = AppleLoginRes.from(authToken);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.expirationTime()).isEqualTo(3600L);
    }

    @Test
    void 문자_발송_응답을_생성한다() {
        SendSmsRes response = SendSmsRes.from("01012345678");

        assertThat(response.phoneNumber()).isEqualTo("01012345678");
    }

    @Test
    void 문자_검증_응답을_생성한다() {
        VerifySmsRes response = VerifySmsRes.from("123456");

        assertThat(response.phoneNumber()).isEqualTo("123456");
    }

    @Test
    void 카카오_유저_응답을_도메인_DTO로_변환한다() {
        KaKaoUserInfoRes response = new KaKaoUserInfoRes(1L, new KaKaoUserInfoRes.KakaoAccount("test@example.com", "테스터"));

        KakaoUserInfoDTO kakaoUserInfoDTO = response.toKaKaoUserInfoDTO();

        assertThat(kakaoUserInfoDTO.kakaoId()).isEqualTo(1L);
        assertThat(kakaoUserInfoDTO.email()).isEqualTo("test@example.com");
        assertThat(kakaoUserInfoDTO.name()).isEqualTo("테스터");
    }
}
