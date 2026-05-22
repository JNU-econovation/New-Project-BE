package com.econo_4factorial.newproject.auth.jwt.service;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.InvalidRefreshTokenException;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.LoggedOutTokenException;
import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import com.econo_4factorial.newproject.auth.jwt.BlacklistToken;
import com.econo_4factorial.newproject.auth.jwt.RefreshToken;
import com.econo_4factorial.newproject.auth.jwt.TokenType;
import com.econo_4factorial.newproject.auth.jwt.repository.BlacklistTokenRepository;
import com.econo_4factorial.newproject.auth.jwt.repository.RefreshTokenRepository;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistTokenRepository blacklistTokenRepository;
    @Value("${custom.jwt.access.expired-time}")
    private long accessTokenExpiredTime;
    @Value("${custom.jwt.refresh.expired-time}")
    private long refreshTokenExpiredTime;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthToken issueAuthToken(Long userId) {
        Date now = new Date();
        String accessToken = jwtTokenProvider.issueAccessToken(userId);
        String refreshToken = jwtTokenProvider.issueRefreshToken(userId);

        saveRefreshToken(userId, refreshToken);

        return AuthToken.of(accessToken, refreshToken,
                now.getTime() + Duration.ofSeconds(accessTokenExpiredTime).toMillis());
    }

    @Transactional
    public AuthToken reissue(String refreshToken) {
        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);

        if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        refreshTokenRepository.deleteById(userId);

        return issueAuthToken(userId);
    }

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        RefreshToken token = new RefreshToken(userId, refreshToken, refreshTokenExpiredTime);
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void logout(Long userId, String accessToken) {
        refreshTokenRepository.deleteById(userId);

        Long expiration = jwtTokenProvider.getExpirationTime(accessToken, TokenType.ACCESS);
        Long now = new Date().getTime();
        BlacklistToken blacklistToken = BlacklistToken.builder()
                .accessToken(accessToken)
                .status("logout")
                .expirationTime((expiration - now) / 1000)
                .build();
        blacklistTokenRepository.save(blacklistToken);
    }

    @Transactional(readOnly = true)
    public boolean isLoggedIn(Long userId, String accessToken) {
        if (blacklistTokenRepository.existsById(accessToken)) {
            throw new LoggedOutTokenException();
        }

        if (!refreshTokenRepository.existsById(userId)) {
            throw new LoggedOutTokenException();
        }
        return true;
    }
}
