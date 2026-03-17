package com.econo_4factorial.newproject.auth.service;

import com.econo_4factorial.newproject.auth.dto.Req.AppleLoginReq;
import com.econo_4factorial.newproject.auth.dto.Req.FullName;
import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.AuthException;
import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
import com.econo_4factorial.newproject.auth.service.apple.AppleOAuthService;
import com.econo_4factorial.newproject.auth.service.kakao.KaKaoOAuthService;
import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.common.exception.CommonErrorType;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private KaKaoOAuthService kaKaoOAuthService;

    @Mock
    private AppleOAuthService appleOAuthService;

    @Mock
    private AuthTokenService authTokenService;

    private OAuthService oAuthService;

    @BeforeEach
    void setUp() {
        oAuthService = new OAuthService(userService, kaKaoOAuthService, appleOAuthService, authTokenService);
    }

    @Test
    void 카카오_로그인_URI를_위임한다() {
        given(kaKaoOAuthService.getLoginURI()).willReturn("https://kakao/login");

        String loginUri = oAuthService.getKakaoLoginURI();

        assertThat(loginUri).isEqualTo("https://kakao/login");
    }

    @Test
    void 카카오_로그인_후_인증토큰을_발급한다() {
        KakaoUserInfoDTO userInfoDTO = new KakaoUserInfoDTO(1L, "test@example.com", "홍길동");
        User user = User.kakaoUserBuilder().kakaoId(1L).email("test@example.com").name("홍길동").build();
        ReflectionTestUtils.setField(user, "id", 10L);
        AuthToken authToken = AuthToken.of("access-token", "refresh-token", 3600L);
        given(kaKaoOAuthService.getUserInfo("auth-code")).willReturn(userInfoDTO);
        given(userService.findOrCreateUserByKakaoUserInfo(userInfoDTO)).willReturn(user);
        given(authTokenService.issueAuthToken(10L)).willReturn(authToken);

        AuthToken result = oAuthService.loginWithKaKao("auth-code");

        assertThat(result).isEqualTo(authToken);
        verify(authTokenService).issueAuthToken(10L);
    }

    @Test
    void 카카오_로그인_중_잘못된_요청예외가_발생하면_AuthException으로_변환한다() {
        given(kaKaoOAuthService.getUserInfo("auth-code"))
                .willThrow(new BadRequestException(CommonErrorType.METHOD_ARGUMENT_NOT_VALID_EXCEPTION));

        assertThatThrownBy(() -> oAuthService.loginWithKaKao("auth-code"))
                .isInstanceOf(AuthException.class);
    }

    @Test
    void 애플_로그인_후_인증토큰을_발급한다() {
        AppleLoginReq request = new AppleLoginReq("identity-token", "test@example.com", new FullName("홍", "길동"));
        AppleUserInfoDTO userInfoDTO = new AppleUserInfoDTO("apple-sub", "홍길동", "test@example.com");
        User user = User.appleUserBuilder().appleSub("apple-sub").email("test@example.com").name("홍길동").build();
        ReflectionTestUtils.setField(user, "id", 20L);
        AuthToken authToken = AuthToken.of("access-token", "refresh-token", 3600L);
        given(appleOAuthService.getUserInfo(request)).willReturn(userInfoDTO);
        given(userService.findOrCreateUserByAppleUserInfo(userInfoDTO)).willReturn(user);
        given(authTokenService.issueAuthToken(20L)).willReturn(authToken);

        AuthToken result = oAuthService.loginWithApple(request);

        assertThat(result).isEqualTo(authToken);
        verify(authTokenService).issueAuthToken(20L);
    }

    @Test
    void 애플_로그인_중_잘못된_요청예외가_발생하면_AuthException으로_변환한다() {
        AppleLoginReq request = new AppleLoginReq("identity-token", "test@example.com", new FullName("홍", "길동"));
        given(appleOAuthService.getUserInfo(request))
                .willThrow(new BadRequestException(CommonErrorType.METHOD_ARGUMENT_NOT_VALID_EXCEPTION));

        assertThatThrownBy(() -> oAuthService.loginWithApple(request))
                .isInstanceOf(AuthException.class);
    }

    @Test
    void 로그아웃을_위임한다() {
        oAuthService.logout(1L, "access-token");

        verify(authTokenService).logout(1L, "access-token");
    }

    @Test
    void 토큰_재발급을_위임한다() {
        AuthToken authToken = AuthToken.of("access-token", "refresh-token", 3600L);
        given(authTokenService.reissue("refresh-token")).willReturn(authToken);

        AuthToken result = oAuthService.reissue("refresh-token");

        assertThat(result).isEqualTo(authToken);
    }
}
