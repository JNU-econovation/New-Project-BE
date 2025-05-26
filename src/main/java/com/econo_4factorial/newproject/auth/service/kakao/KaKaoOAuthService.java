package com.econo_4factorial.newproject.auth.service.kakao;

import com.econo_4factorial.newproject.auth.dto.kakao.KaKaoUserInfoRes;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class KaKaoOAuthService {
    private final KaKaoOAuthFeignClient kaKaoOAuthFeignClient;
    private final KakaoUserInfoFeignClient kakaoUserInfoFeignClient;

    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String TOKEN_PRIFIX = "bearer ";
    private static final String PROPERTIES = "[\"kakao_account.name\", \"kakao_account.email\"]";

    @Value("${oauth.kakao.client-id}")
    private String client_id;

    @Value("${oauth.kakao.uri.redirect-uri}")
    private String redirect_uri;

    public String getLoginURI() {
        return UriComponentsBuilder
                .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", client_id)
                .queryParam("redirect_uri", redirect_uri)
                .queryParam("response_type", RESPONSE_TYPE)
                .build()
                .toUriString();
    }

    public KakaoUserInfoDTO getUserInfo(String kakaoAuthorizationCode) {
        String kakaoAccessToken = getAccessToken(kakaoAuthorizationCode);
        KaKaoUserInfoRes kaKaoUserInfo = kakaoUserInfoFeignClient.getUserInfo(TOKEN_PRIFIX + kakaoAccessToken, PROPERTIES );
        return kaKaoUserInfo.toKaKaoUserInfoDTO();
    }

    private String getAccessToken(String kakaoAuthorizationCode) {
        return kaKaoOAuthFeignClient.getAccessToken(
                GRANT_TYPE,
                client_id,
                redirect_uri,
                kakaoAuthorizationCode
        ).accessToken();
    }


}
