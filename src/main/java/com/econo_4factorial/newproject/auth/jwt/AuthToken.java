package com.econo_4factorial.newproject.auth.jwt;

public record AuthToken (
    String accessToken,
    String refreshToken,
    String grantType,
    Long expirationTime
) {
    public static AuthToken of(String accessToken, String refreshToken, String tokenHeader, long accessTokenExpiredTime) {
        return new AuthToken(accessToken, refreshToken, tokenHeader, accessTokenExpiredTime);
    }
}