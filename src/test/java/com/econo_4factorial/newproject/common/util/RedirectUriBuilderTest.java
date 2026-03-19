package com.econo_4factorial.newproject.common.util;

import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class RedirectUriBuilderTest {

    private RedirectUriBuilder redirectUriBuilder;

    @BeforeEach
    void setUp() {
        redirectUriBuilder = new RedirectUriBuilder();
        ReflectionTestUtils.setField(redirectUriBuilder, "baseUri", "https://example.com/oauth/callback");
    }

    @Test
    void 로그인_성공_URI를_생성한다() {
        AuthToken authToken = AuthToken.of("access-token", "refresh-token", 3600L);

        String uri = redirectUriBuilder.buildLoginSuccessUri(authToken);

        assertThat(uri).startsWith("https://example.com/oauth/callback");
        assertThat(uri).contains("accessToken=access-token");
        assertThat(uri).contains("refreshToken=refresh-token");
        assertThat(uri).contains("expiredTime=3600");
    }

    @Test
    void 로그인_실패_URI를_생성한다() {
        String uri = redirectUriBuilder.buildLoginFailUri();

        assertThat(uri).isEqualTo("https://example.com/oauth/callback");
    }
}
