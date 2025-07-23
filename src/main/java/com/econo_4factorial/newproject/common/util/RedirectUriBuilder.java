package com.econo_4factorial.newproject.common.util;


import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class RedirectUriBuilder {
    @Value("${auth.login_success.base_uri}")
    private String baseUri;

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
