package com.econo_4factorial.newproject.auth.jwt.service;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.InvalidRefreshTokenException;
import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import com.econo_4factorial.newproject.auth.jwt.RefreshToken;
import com.econo_4factorial.newproject.auth.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${custom.jwt.access.expiredTime}")
    private long accessTokenExpiredTime;
    @Value("${custom.jwt.refresh.expiredTime}")
    private long refreshTokenExpiredTime;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthToken issueAuthToken(Long userId) {
        Date now = new Date();
        String accessToken = jwtTokenProvider.issueAccessToken(userId);
        String refreshToken = jwtTokenProvider.issueRefreshToken(userId);

        saveRefreshToken(userId, refreshToken);

        return AuthToken.of(accessToken, refreshToken, now.getTime() + Duration.ofSeconds(accessTokenExpiredTime).toMillis());
    }

    @Transactional
    public AuthToken reissue(String refreshToken) {
        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);

        if (!refreshTokenRepository.existsByRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        return issueAuthToken(userId);
    }

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        RefreshToken token = new RefreshToken(userId, refreshToken, refreshTokenExpiredTime);
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
