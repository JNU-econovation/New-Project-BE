package com.econo_4factorial.newproject.auth.jwt;

public record AuthToken (
    String accessToken,
    String refreshToken,
    Long expirationTime
) {
    public static AuthToken of(String accessToken, String refreshToken, long accessTokenExpiredTime) {
        return new AuthToken(accessToken, refreshToken, accessTokenExpiredTime);
    }
}