package com.econo_4factorial.newproject.auth.jwt.service;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.ExpiredTokenException;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.SignatureException;
import com.econo_4factorial.newproject.auth.jwt.TokenType;
import com.econo_4factorial.newproject.auth.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("만료된 토큰을 파싱하면 ExpiredTokenException이 발생한다")
    void getUserIdFromAccessToken_fail_expiredToken() {
        // given: 만료 시간이 과거인 토큰 생성
        SecretKey key = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        String expiredToken = Jwts.builder()
                .claim("id", userId)
                .issuedAt(new Date(System.currentTimeMillis() - 10000))
                .expiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(key)
                .compact();

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getUserIdFromAccessToken(expiredToken))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("다른 비밀키로 서명된 토큰을 파싱하면 SignatureException이 발생한다")
    void getUserIdFromAccessToken_fail_invalidSignature() {
        // given: 다른 비밀키로 서명된 토큰 생성
        SecretKey wrongKey = Keys.hmacShaKeyFor("wrongSecretKeyThatIsAlsoLongEnoughValue".getBytes());
        String invalidToken = Jwts.builder()
                .claim("id", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(wrongKey)
                .compact();

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getUserIdFromAccessToken(invalidToken))
                .isInstanceOf(SignatureException.class);
    }
}
