package com.econo_4factorial.newproject.common.annotation.resolver;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.NotExistTokenException;
import com.econo_4factorial.newproject.auth.jwt.service.JwtTokenProvider;
import com.econo_4factorial.newproject.common.annotation.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserIdResolverTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private UserIdResolver userIdResolver;

    @BeforeEach
    void setUp() {
        userIdResolver = new UserIdResolver(jwtTokenProvider);
    }

    @Test
    void UserId_애노테이션이_있으면_지원한다() throws Exception {
        Method method = SampleController.class.getDeclaredMethod("withUserId", Long.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        boolean supported = userIdResolver.supportsParameter(parameter);

        assertThat(supported).isTrue();
    }

    @Test
    void UserId_애노테이션이_없으면_지원하지_않는다() throws Exception {
        Method method = SampleController.class.getDeclaredMethod("withoutUserId", Long.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        boolean supported = userIdResolver.supportsParameter(parameter);

        assertThat(supported).isFalse();
    }

    @Test
    void Authorization_헤더가_없으면_예외가_발생한다() throws Exception {
        Method method = SampleController.class.getDeclaredMethod("withUserId", Long.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        NativeWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest());

        assertThatThrownBy(() -> userIdResolver.resolveArgument(parameter, null, webRequest, null))
                .isInstanceOf(NotExistTokenException.class);
    }

    @Test
    void Authorization_헤더에서_유저_ID를_추출한다() throws Exception {
        Method method = SampleController.class.getDeclaredMethod("withUserId", Long.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer access-token");
        NativeWebRequest webRequest = new ServletWebRequest(request);
        when(jwtTokenProvider.extractToken("Bearer access-token")).thenReturn("access-token");
        when(jwtTokenProvider.getUserIdFromAccessToken("access-token")).thenReturn(1L);

        Object resolved = userIdResolver.resolveArgument(parameter, null, webRequest, null);

        assertThat(resolved).isEqualTo(1L);
        verify(jwtTokenProvider).extractToken("Bearer access-token");
        verify(jwtTokenProvider).getUserIdFromAccessToken("access-token");
    }

    private static class SampleController {
        void withUserId(@UserId Long userId) {
        }

        void withoutUserId(Long userId) {
        }
    }
}
