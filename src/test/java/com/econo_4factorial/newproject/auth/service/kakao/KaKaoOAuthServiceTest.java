package com.econo_4factorial.newproject.auth.service.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.econo_4factorial.newproject.auth.dto.kakao.KaKaoUserInfoRes;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoTokenRes;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class KaKaoOAuthServiceTest {

    @Mock
    private KaKaoOAuthFeignClient kaKaoOAuthFeignClient;

    @Mock
    private KakaoUserInfoFeignClient kakaoUserInfoFeignClient;

    private KaKaoOAuthService kaKaoOAuthService;

    @BeforeEach
    void setUp() {
        kaKaoOAuthService = new KaKaoOAuthService(kaKaoOAuthFeignClient, kakaoUserInfoFeignClient);
        ReflectionTestUtils.setField(kaKaoOAuthService, "client_id", "kakao-client-id");
        ReflectionTestUtils.setField(kaKaoOAuthService, "redirect_uri", "https://example.com/oauth/kakao");
    }

    @Test
    void 카카오_로그인_URI를_생성한다() {
        String loginUri = kaKaoOAuthService.getLoginURI();

        assertThat(loginUri).startsWith("https://kauth.kakao.com/oauth/authorize");
        assertThat(loginUri).contains("client_id=kakao-client-id");
        assertThat(loginUri).contains("redirect_uri=https://example.com/oauth/kakao");
        assertThat(loginUri).contains("response_type=code");
    }

    @Test
    void 인가코드로_카카오_유저정보를_조회한다() {
        given(kaKaoOAuthFeignClient.getAccessToken("authorization_code", "kakao-client-id",
                "https://example.com/oauth/kakao", "auth-code"))
                .willReturn(new KakaoTokenRes("bearer", "kakao-access-token", 3600, "refresh-token", 7200));
        given(kakaoUserInfoFeignClient.getUserInfo("bearer kakao-access-token",
                "[\"kakao_account.name\", \"kakao_account.email\"]"))
                .willReturn(new KaKaoUserInfoRes(1L, new KaKaoUserInfoRes.KakaoAccount("test@example.com", "홍길동")));

        KakaoUserInfoDTO userInfo = kaKaoOAuthService.getUserInfo("auth-code");

        assertThat(userInfo.kakaoId()).isEqualTo(1L);
        assertThat(userInfo.email()).isEqualTo("test@example.com");
        assertThat(userInfo.name()).isEqualTo("홍길동");
        verify(kaKaoOAuthFeignClient).getAccessToken("authorization_code", "kakao-client-id",
                "https://example.com/oauth/kakao", "auth-code");
        verify(kakaoUserInfoFeignClient).getUserInfo("bearer kakao-access-token",
                "[\"kakao_account.name\", \"kakao_account.email\"]");
    }
}
