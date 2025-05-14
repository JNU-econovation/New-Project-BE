package com.econo_4factorial.newproject.auth.jwt.service;

import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthTokenService {

    private static final String TOKEN_HEADER = "Bearer";

    @Value("${custom.jwt.access.expiredTime}")
    private long accessTokenExpiredTime;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthToken issueAuthToken(Long userId) {
        String accessToken = jwtTokenProvider.issueAccessToken(userId);
        String refreshToken = jwtTokenProvider.issueRefreshToken(userId);

        return AuthToken.of(accessToken, refreshToken, TOKEN_HEADER, accessTokenExpiredTime);
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
