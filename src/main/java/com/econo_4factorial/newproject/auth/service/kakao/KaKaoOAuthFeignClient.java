package com.econo_4factorial.newproject.auth.service.kakao;

import com.econo_4factorial.newproject.auth.dto.kakao.KakaoTokenRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="kakaoOAuthFeignClient", url="https://kauth.kakao.com/oauth")
public interface KaKaoOAuthFeignClient {

    @PostMapping("/token")
    KakaoTokenRes getAccessToken(@RequestParam("grant_type") String grantType,
                                       @RequestParam("client_id") String clientId,
                                       @RequestParam("redirect_uri") String redirectUri,
                                       @RequestParam("code") String kakaoAuthorizationCode);
}
