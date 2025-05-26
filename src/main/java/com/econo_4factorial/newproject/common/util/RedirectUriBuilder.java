package com.econo_4factorial.newproject.common.util;


import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

@UtilityClass
public class RedirectUriBuilder {
    private final String baseUri = "http://soop.euichan.com/social-login-loading";

    public String buildLoginSuccessUri (AuthToken authToken) {
        return UriComponentsBuilder.fromUriString(baseUri)
                .queryParam("accessToken", authToken.accessToken())
                .queryParam("refreshToken", authToken.refreshToken())
                .queryParam("expiredTime", authToken.expirationTime())
                .build()
                .toUriString();
    }

    public String buildLoginFailUri () {
        return UriComponentsBuilder.fromUriString(baseUri)
                .build()
                .toUriString();
    }
}
