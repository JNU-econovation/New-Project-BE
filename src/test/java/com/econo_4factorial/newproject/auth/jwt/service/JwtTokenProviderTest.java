package com.econo_4factorial.newproject.auth.jwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.ExpiredTokenException;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.SignatureException;
import com.econo_4factorial.newproject.auth.jwt.TokenType;
import com.econo_4factorial.newproject.auth.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void 액세스_토큰을_정상적으로_발급하고_유저_ID를_추출한다() {
        String token = jwtTokenProvider.issueAccessToken(userId);
        Long extractedId = jwtTokenProvider.getUserIdFromAccessToken(token);

        assertThat(token).isNotBlank();
        assertThat(extractedId).isEqualTo(userId);
    }

    @Test
    void 리프레시_토큰을_정상적으로_발급하고_유저_ID를_추출한다() {
        String token = jwtTokenProvider.issueRefreshToken(userId);
        Long extractedId = jwtTokenProvider.getUserIdFromRefreshToken(token);

        assertThat(token).isNotBlank();
        assertThat(extractedId).isEqualTo(userId);
    }

    @Test
    void 토큰의_만료_시간을_정상적으로_추출한다() {
        String token = jwtTokenProvider.issueAccessToken(userId);
        long now = new Date().getTime();

        Long expirationTime = jwtTokenProvider.getExpirationTime(token, TokenType.ACCESS);

        assertThat(expirationTime).isGreaterThan(now);
        assertThat(expirationTime - now).isLessThanOrEqualTo(accessTokenExpiredTime * 1000);
    }

    @Test
    void Bearer_헤더에서_토큰을_정상적으로_추출한다() {
        String authHeader = "Bearer some_jwt_token_value";

        String extracted = jwtTokenProvider.extractToken(authHeader);

        assertThat(extracted).isEqualTo("some_jwt_token_value");
    }

    @Test
    void Bearer_형식이_아닌_헤더는_예외가_발생한다() {
        assertThatThrownBy(() -> jwtTokenProvider.extractToken("Basic token"))
                .isInstanceOf(
                        com.econo_4factorial.newproject.auth.exception.BadRequestException.InvalidTokenHeaderException.class);
    }

    @Test
    void null_헤더는_예외가_발생한다() {
        assertThatThrownBy(() -> jwtTokenProvider.extractToken(null))
                .isInstanceOf(
                        com.econo_4factorial.newproject.auth.exception.BadRequestException.InvalidTokenHeaderException.class);
    }

    @Test
    void 올바른_리프레시_토큰을_검증하면_true를_반환한다() {
        String token = jwtTokenProvider.issueRefreshToken(userId);

        boolean isValid = jwtTokenProvider.validateRefreshToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void 만료된_토큰을_파싱하면_ExpiredTokenException이_발생한다() {
        SecretKey key = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        String expiredToken = Jwts.builder()
                .claim("id", userId)
                .issuedAt(new Date(System.currentTimeMillis() - 10000))
                .expiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(key)
                .compact();

        assertThatThrownBy(() -> jwtTokenProvider.getUserIdFromAccessToken(expiredToken))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    void 다른_비밀키로_서명된_토큰을_파싱하면_SignatureException이_발생한다() {
        SecretKey wrongKey = Keys.hmacShaKeyFor("wrongSecretKeyThatIsAlsoLongEnoughValue".getBytes());
        String invalidToken = Jwts.builder()
                .claim("id", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(wrongKey)
                .compact();

        assertThatThrownBy(() -> jwtTokenProvider.getUserIdFromAccessToken(invalidToken))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void 다른_비밀키로_서명된_리프레시_토큰은_검증에_실패한다() {
        SecretKey wrongKey = Keys.hmacShaKeyFor("wrongSecretKeyThatIsAlsoLongEnoughValue".getBytes());
        String invalidToken = Jwts.builder()
                .claim("id", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(wrongKey)
                .compact();

        assertThatThrownBy(() -> jwtTokenProvider.validateRefreshToken(invalidToken))
                .isInstanceOf(SignatureException.class);
    }
}
