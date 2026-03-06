package com.econo_4factorial.newproject.auth.jwt.service;

import com.econo_4factorial.newproject.auth.jwt.TokenType;
import com.econo_4factorial.newproject.auth.jwt.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private final String accessSecretKey = "testAccessSecretKeyThatIsAtLeast32CharsLong";
    private final String refreshSecretKey = "testRefreshSecretKeyThatIsAlsoLongEnough";
    private final Long accessTokenExpiredTime = 3600L;
    private final Long refreshTokenExpiredTime = 1209600L;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(
                accessSecretKey,
                refreshSecretKey,
                accessTokenExpiredTime,
                refreshTokenExpiredTime,
                refreshTokenRepository
        );
    }

    @Test
    @DisplayName("액세스 토큰을 정상적으로 발급하고 유저 ID를 추출한다")
    void issueAccessToken_and_getUserId_success() {
        // when
        String token = jwtTokenProvider.issueAccessToken(userId);
        Long extractedId = jwtTokenProvider.getUserIdFromAccessToken(token);

        // then
        assertThat(token).isNotBlank();
        assertThat(extractedId).isEqualTo(userId);
    }

    @Test
    @DisplayName("리프레쉬 토큰을 정상적으로 발급하고 유저 ID를 추출한다")
    void issueRefreshToken_and_getUserId_success() {
        // when
        String token = jwtTokenProvider.issueRefreshToken(userId);
        Long extractedId = jwtTokenProvider.getUserIdFromRefreshToken(token);

        // then
        assertThat(token).isNotBlank();
        assertThat(extractedId).isEqualTo(userId);
    }

    @Test
    @DisplayName("토큰의 만료 시간을 정상적으로 추출한다")
    void getExpirationTime_success() {
        // given
        String token = jwtTokenProvider.issueAccessToken(userId);
        long now = new Date().getTime();

        // when
        Long expirationTime = jwtTokenProvider.getExpirationTime(token, TokenType.ACCESS);

        // then
        assertThat(expirationTime).isGreaterThan(now);
        // 설정한 1시간(3600초) 근처인지 확인 (오차 감안)
        assertThat(expirationTime - now).isLessThanOrEqualTo(accessTokenExpiredTime * 1000);
    }

    @Test
    @DisplayName("Bearer 헤더에서 토큰을 정상적으로 추출한다")
    void extractToken_success() {
        // given
        String authHeader = "Bearer some_jwt_token_value";

        // when
        String extracted = jwtTokenProvider.extractToken(authHeader);

        // then
        assertThat(extracted).isEqualTo("some_jwt_token_value");
    }

    @Test
    @DisplayName("올바른 리프레쉬 토큰을 검증하면 true를 반환한다")
    void validateRefreshToken_success() {
        // given
        String token = jwtTokenProvider.issueRefreshToken(userId);

        // when
        boolean isValid = jwtTokenProvider.validateRefreshToken(token);

        // then
        assertThat(isValid).isTrue();
    }
}
