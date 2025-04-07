package com.econo_4factorial.newproject.auth.jwt;

public record AuthToken (
    String accessToken,
    String refreshToken,
    String grantType,
    Long expirationTime
) {}