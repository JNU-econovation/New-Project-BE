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
import org.junit.jupiter.api.DisplayName;
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
        // @Value 필드 수동 주입
        ReflectionTestUtils.setField(authTokenService, "accessTokenExpiredTime", 3600L);
        ReflectionTestUtils.setField(authTokenService, "refreshTokenExpiredTime", 1209600L);
    }

    @Test
    @DisplayName("로그인 시 토큰 발급 및 리프레쉬 토큰을 저장한다")
    void issueAuthToken_success() {
        // given
        given(jwtTokenProvider.issueAccessToken(userId)).willReturn(accessToken);
        given(jwtTokenProvider.issueRefreshToken(userId)).willReturn(refreshToken);

        // when
        AuthToken result = authTokenService.issueAuthToken(userId);

        // then
        assertThat(result.accessToken()).isEqualTo(accessToken);
        assertThat(result.refreshToken()).isEqualTo(refreshToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("재발급 시 RTR 방식에 따라 이전 토큰을 삭제하고 새 토큰을 발급한다")
    void reissue_success() {
        // given
        given(jwtTokenProvider.getUserIdFromRefreshToken(refreshToken)).willReturn(userId);
        given(refreshTokenRepository.existsByRefreshToken(refreshToken)).willReturn(true);
        given(jwtTokenProvider.issueAccessToken(userId)).willReturn("new_access_token");
        given(jwtTokenProvider.issueRefreshToken(userId)).willReturn("new_refresh_token");

        // when
        AuthToken result = authTokenService.reissue(refreshToken);

        // then
        verify(refreshTokenRepository).deleteById(userId); // 이전 토큰 삭제(RTR) 확인
        verify(refreshTokenRepository).save(any(RefreshToken.class)); // 새 토큰 저장 확인
        assertThat(result.accessToken()).isEqualTo("new_access_token");
        assertThat(result.refreshToken()).isEqualTo("new_refresh_token");
    }

    @Test
    @DisplayName("존재하지 않는 리프레쉬 토큰으로 재발급 시도 시 예외가 발생한다")
    void reissue_fail_invalid_token() {
        // given
        given(jwtTokenProvider.getUserIdFromRefreshToken(refreshToken)).willReturn(userId);
        given(refreshTokenRepository.existsByRefreshToken(refreshToken)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authTokenService.reissue(refreshToken))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }

    @Test
    @DisplayName("로그아웃 시 리프레쉬 토큰을 삭제하고 액세스 토큰을 블랙리스트에 저장한다")
    void logout_success() {
        // given
        long now = new Date().getTime();
        long expirationTime = now + 3600000; // 1시간 뒤
        given(jwtTokenProvider.getExpirationTime(accessToken, TokenType.ACCESS)).willReturn(expirationTime);

        // when
        authTokenService.logout(userId, accessToken);

        // then
        verify(refreshTokenRepository).deleteById(userId);
        verify(blacklistTokenRepository).save(any(BlacklistToken.class));
    }

    @Test
    @DisplayName("블랙리스트에 등록된 토큰으로 접근 시 예외가 발생한다")
    void isLoggedIn_fail_blacklisted() {
        // given
        given(blacklistTokenRepository.existsById(accessToken)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authTokenService.isLoggedIn(userId, accessToken))
                .isInstanceOf(LoggedOutTokenException.class);
    }

    @Test
    @DisplayName("정상 로그인 상태에서는 예외 없이 true를 반환한다")
    void isLoggedIn_success() {
        // given
        given(blacklistTokenRepository.existsById(accessToken)).willReturn(false);
        given(refreshTokenRepository.existsById(userId)).willReturn(true);

        // when
        boolean result = authTokenService.isLoggedIn(userId, accessToken);

        // then
        assertThat(result).isTrue();
    }
}
