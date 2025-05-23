package com.econo_4factorial.newproject.auth.jwt.service;

import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokenService {

    @Value("${custom.jwt.access.expiredTime}")
    private long accessTokenExpiredTime;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthToken issueAuthToken(Long userId) {
        Date now = new Date();
        String accessToken = jwtTokenProvider.issueAccessToken(userId);
        String refreshToken = jwtTokenProvider.issueRefreshToken(userId);

        return AuthToken.of(accessToken, refreshToken, now.getTime() + Duration.ofSeconds(accessTokenExpiredTime).toMillis());
    }

    @Transactional
    public AuthToken reissue(String refreshToken) {
        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);

        if (!jwtTokenProvider.existRefreshTokenByUserId(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token: " + refreshToken);
        }

        return issueAuthToken(userId);
    }

    @Transactional
    public void logout(Long userId) {
        jwtTokenProvider.deleteRefreshTokenByUserId(userId);
    }

}
