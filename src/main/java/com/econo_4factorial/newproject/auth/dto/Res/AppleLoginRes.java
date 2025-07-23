package com.econo_4factorial.newproject.auth.dto.Res;

import com.econo_4factorial.newproject.auth.jwt.AuthToken;

public record AppleLoginRes (
        String accessToken,
        String refreshToken,
        Long expirationTime
){
    public static AppleLoginRes from (AuthToken authToken) {
        return new AppleLoginRes(authToken.accessToken(), authToken.refreshToken(), authToken.expirationTime());
    }
}
