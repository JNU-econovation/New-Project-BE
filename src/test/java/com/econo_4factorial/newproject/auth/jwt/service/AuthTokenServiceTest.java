package com.econo_4factorial.newproject.auth.jwt.service;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.InvalidRefreshTokenException;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.LoggedOutTokenException;
import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import com.econo_4factorial.newproject.auth.jwt.BlacklistToken;
import com.econo_4factorial.newproject.auth.jwt.RefreshToken;
import com.econo_4factorial.newproject.auth.jwt.TokenType;
import com.econo_4factorial.newproject.auth.jwt.repository.BlacklistTokenRepository;
import com.econo_4factorial.newproject.auth.jwt.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceTest {

    @InjectMocks
    private AuthTokenService authTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private BlacklistTokenRepository blacklistTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private final Long userId = 1L;
    private final String accessToken = "mock_access_token";
    private final String refreshToken = "mock_refresh_token";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authTokenService, "accessTokenExpiredTime", 3600L);
        ReflectionTestUtils.setField(authTokenService, "refreshTokenExpiredTime", 1209600L);
    }

    @Test
    void 로그인_시_토큰을_발급하고_리프레시_토큰을_저장한다() {
        given(jwtTokenProvider.issueAccessToken(userId)).willReturn(accessToken);
        given(jwtTokenProvider.issueRefreshToken(userId)).willReturn(refreshToken);

        AuthToken result = authTokenService.issueAuthToken(userId);

        assertThat(result.accessToken()).isEqualTo(accessToken);
        assertThat(result.refreshToken()).isEqualTo(refreshToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void 리프레시_토큰_저장시_유저ID와_TTL을_포함한다() {
        authTokenService.saveRefreshToken(userId, refreshToken);

        verify(refreshTokenRepository).save(argThat(savedToken ->
                savedToken.getId().equals(userId)
                        && savedToken.getRefreshToken().equals(refreshToken)
                        && savedToken.getExpirationTime().equals(1209600L)
        ));
    }

    @Test
    void 재발급_시_이전_토큰을_삭제하고_새_토큰을_발급한다() {
        given(jwtTokenProvider.getUserIdFromRefreshToken(refreshToken)).willReturn(userId);
        given(refreshTokenRepository.existsByRefreshToken(refreshToken)).willReturn(true);
        given(jwtTokenProvider.issueAccessToken(userId)).willReturn("new_access_token");
        given(jwtTokenProvider.issueRefreshToken(userId)).willReturn("new_refresh_token");

        AuthToken result = authTokenService.reissue(refreshToken);

        verify(refreshTokenRepository).deleteById(userId);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        assertThat(result.accessToken()).isEqualTo("new_access_token");
        assertThat(result.refreshToken()).isEqualTo("new_refresh_token");
    }

    @Test
    void 존재하지_않는_리프레시_토큰으로_재발급을_시도하면_예외가_발생한다() {
        given(jwtTokenProvider.getUserIdFromRefreshToken(refreshToken)).willReturn(userId);
        given(refreshTokenRepository.existsByRefreshToken(refreshToken)).willReturn(false);

        assertThatThrownBy(() -> authTokenService.reissue(refreshToken))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }

    @Test
    void 로그아웃_시_리프레시_토큰을_삭제하고_액세스_토큰을_블랙리스트에_저장한다() {
        long now = new Date().getTime();
        long expirationTime = now + 3600000;
        given(jwtTokenProvider.getExpirationTime(accessToken, TokenType.ACCESS)).willReturn(expirationTime);

        authTokenService.logout(userId, accessToken);

        verify(refreshTokenRepository).deleteById(userId);
        verify(blacklistTokenRepository).save(argThat(blacklistToken ->
                blacklistToken.getAccessToken().equals(accessToken)
                        && blacklistToken.getStatus().equals("logout")
                        && blacklistToken.getExpirationTime() > 0
        ));
    }

    @Test
    void 블랙리스트에_등록된_토큰으로_접근하면_예외가_발생한다() {
        given(blacklistTokenRepository.existsById(accessToken)).willReturn(true);

        assertThatThrownBy(() -> authTokenService.isLoggedIn(userId, accessToken))
                .isInstanceOf(LoggedOutTokenException.class);
    }

    @Test
    void 정상_로그인_상태에서는_true를_반환한다() {
        given(blacklistTokenRepository.existsById(accessToken)).willReturn(false);
        given(refreshTokenRepository.existsById(userId)).willReturn(true);

        boolean result = authTokenService.isLoggedIn(userId, accessToken);

        assertThat(result).isTrue();
    }

    @Test
    void 리프레시_토큰이_없으면_로그아웃_예외가_발생한다() {
        given(blacklistTokenRepository.existsById(accessToken)).willReturn(false);
        given(refreshTokenRepository.existsById(userId)).willReturn(false);

        assertThatThrownBy(() -> authTokenService.isLoggedIn(userId, accessToken))
                .isInstanceOf(LoggedOutTokenException.class);
    }
}
