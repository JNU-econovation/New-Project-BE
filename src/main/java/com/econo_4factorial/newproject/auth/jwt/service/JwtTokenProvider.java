package com.econo_4factorial.newproject.auth.jwt.service;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.ExpiredTokenException;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.SignatureException;
import com.econo_4factorial.newproject.auth.jwt.TokenType;
import com.econo_4factorial.newproject.auth.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String AUTH_TOKEN_HEADER = "Bearer ";

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final Long accessTokenExpiredTime;
    private final Long refreshTokenExpiredTime;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProvider(
            @Value("${custom.jwt.access.secretkey}") String accessSecretKey,
            @Value("${custom.jwt.refresh.secretKey}") String refreshSecretKey,
            @Value("${custom.jwt.access.expiredTime}") Long accessTokenExpiredTime,
            @Value("${custom.jwt.refresh.expiredTime}") Long refreshTokenExpiredTime,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
        this.accessTokenExpiredTime = accessTokenExpiredTime;
        this.refreshTokenExpiredTime = refreshTokenExpiredTime;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String issueAccessToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .claim("id", userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + Duration.ofSeconds(accessTokenExpiredTime).toMillis()))
                .signWith(accessSecretKey)
                .compact();
    }

    public String issueRefreshToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .claim("id", userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + Duration.ofSeconds(refreshTokenExpiredTime).toMillis()))
                .signWith(refreshSecretKey)
                .compact();
    }

    public Long getUserIdFromAccessToken(String accessToken) {
        Claims claims = getClaimsFromToken(accessToken, TokenType.ACCESS);
        return claims.get("id", Long.class);
    }

    public Long getUserIdFromRefreshToken(String refreshToken) {
        Claims claims = getClaimsFromToken(refreshToken, TokenType.REFRESH);
        return claims.get("id", Long.class);
    }

    public boolean isValidRefreshToken(String refreshToken) {
        return refreshTokenRepository.existsByRefreshToken(refreshToken);
    }

    private Claims getClaimsFromToken(String token, TokenType tokenType) {
        SecretKey secretKey = tokenType.equals(TokenType.ACCESS) ? accessSecretKey : refreshSecretKey;
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new SignatureException();
        }
    }

    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
