package com.econo_4factorial.newproject.common.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
import com.econo_4factorial.newproject.auth.jwt.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtInterceptorTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthTokenService authTokenService;

    private JwtInterceptor jwtInterceptor;

    @BeforeEach
    void setUp() {
        jwtInterceptor = new JwtInterceptor(jwtTokenProvider, authTokenService);
    }

    @Test
    void 프리플라이트_요청은_통과시킨다() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/api/v1/users/profile");
        HttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Origin", "https://example.com");
        request.addHeader("Access-Control-Request-Method", "GET");

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        verifyNoInteractions(jwtTokenProvider, authTokenService);
    }

    @Test
    void 재발급_요청은_통과시킨다() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/oauth/reissue");
        HttpServletResponse response = new MockHttpServletResponse();

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        verifyNoInteractions(jwtTokenProvider, authTokenService);
    }

    @Test
    void 로그인된_요청은_토큰검증_결과를_반환한다() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users/profile");
        HttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer access-token");
        when(jwtTokenProvider.extractToken("Bearer access-token")).thenReturn("access-token");
        when(jwtTokenProvider.getUserIdFromAccessToken("access-token")).thenReturn(1L);
        when(authTokenService.isLoggedIn(1L, "access-token")).thenReturn(true);

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        verify(jwtTokenProvider).extractToken("Bearer access-token");
        verify(jwtTokenProvider).getUserIdFromAccessToken("access-token");
        verify(authTokenService).isLoggedIn(1L, "access-token");
    }

    @Test
    void 로그인되지_않은_요청이면_false를_반환한다() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users/profile");
        HttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer access-token");
        when(jwtTokenProvider.extractToken("Bearer access-token")).thenReturn("access-token");
        when(jwtTokenProvider.getUserIdFromAccessToken("access-token")).thenReturn(1L);
        when(authTokenService.isLoggedIn(1L, "access-token")).thenReturn(false);

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertThat(result).isFalse();
    }
}
