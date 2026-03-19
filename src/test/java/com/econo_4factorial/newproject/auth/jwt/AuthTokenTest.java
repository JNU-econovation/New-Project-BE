package com.econo_4factorial.newproject.auth.jwt;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenTest {

    @Test
    void 액세스_리프레시_토큰과_만료시간을_보관한다() {
        AuthToken authToken = AuthToken.of("access-token", "refresh-token", 3600L);

        assertThat(authToken.accessToken()).isEqualTo("access-token");
        assertThat(authToken.refreshToken()).isEqualTo("refresh-token");
        assertThat(authToken.expirationTime()).isEqualTo(3600L);
    }
}
