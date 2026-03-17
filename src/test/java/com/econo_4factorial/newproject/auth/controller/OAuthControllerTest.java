package com.econo_4factorial.newproject.auth.controller;

import com.econo_4factorial.newproject.auth.dto.Req.AppleLoginReq;
import com.econo_4factorial.newproject.auth.dto.Req.FullName;
import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import com.econo_4factorial.newproject.auth.jwt.service.JwtTokenProvider;
import com.econo_4factorial.newproject.auth.service.OAuthService;
import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OAuthControllerTest {

    @Mock
    private OAuthService oAuthService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RedirectUriBuilder redirectUriBuilder = new RedirectUriBuilder();
        ReflectionTestUtils.setField(redirectUriBuilder, "baseUri", "https://example.com/oauth/callback");
        OAuthController controller = new OAuthController(oAuthService, redirectUriBuilder, jwtTokenProvider);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(redirectUriBuilder);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new UserIdArgumentResolver())
                .setValidator(validator)
                .build();
    }

    @Test
    void 카카오_로그인_URI를_조회한다() throws Exception {
        given(oAuthService.getKakaoLoginURI()).willReturn("https://kauth.kakao.com/oauth/authorize?client_id=test");

        mockMvc.perform(get("/api/v1/oauth/kakao/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.uri").value("https://kauth.kakao.com/oauth/authorize?client_id=test"));
    }

    @Test
    void 카카오_콜백으로_로그인하면_리다이렉트_헤더를_반환한다() throws Exception {
        AuthToken authToken = AuthToken.of("access-token", "refresh-token", 3600L);
        given(oAuthService.loginWithKaKao("auth-code")).willReturn(authToken);

        mockMvc.perform(get("/api/v1/oauth/kakao/callback")
                        .param("code", "auth-code"))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, org.hamcrest.Matchers.containsString("accessToken=access-token")))
                .andExpect(jsonPath("$.status").value("success"));

        verify(oAuthService).loginWithKaKao("auth-code");
    }

    @Test
    void 애플_로그인을_처리한다() throws Exception {
        AuthToken authToken = AuthToken.of("access-token", "refresh-token", 3600L);
        given(oAuthService.loginWithApple(new AppleLoginReq("identity-token", "test@example.com", new FullName("홍", "길동"))))
                .willReturn(authToken);

        mockMvc.perform(post("/api/v1/oauth/apple/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "identityToken": "identity-token",
                                  "email": "test@example.com",
                                  "fullName": {
                                    "familyName": "홍",
                                    "givenName": "길동"
                                  }
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.data.expirationTime").value(3600L));
    }

    @Test
    void 로그아웃을_처리한다() throws Exception {
        given(jwtTokenProvider.extractToken("Bearer access-token")).willReturn("access-token");

        mockMvc.perform(post("/api/v1/oauth/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(oAuthService).logout(1L, "access-token");
    }

    @Test
    void 리프레시_토큰으로_재발급한다() throws Exception {
        AuthToken authToken = AuthToken.of("new-access-token", "new-refresh-token", 7200L);
        given(oAuthService.reissue("refresh-token")).willReturn(authToken);

        mockMvc.perform(post("/api/v1/oauth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "refresh-token"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("new-refresh-token"))
                .andExpect(jsonPath("$.data.expirationTime").value(7200L));
    }

    @Test
    void 리프레시_토큰이_없으면_유효성_예외응답을_반환한다() throws Exception {
        mockMvc.perform(post("/api/v1/oauth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_001"))
                .andExpect(jsonPath("$.message").value("리프레쉬 토큰은 필수입니다."));
    }

    private static class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(UserId.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            return 1L;
        }
    }
}
