package com.econo_4factorial.newproject.auth.service.kakao;

import com.econo_4factorial.newproject.auth.dto.kakao.KaKaoUserInfoRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="kakaoUserInfoFeignClient", url="https://kapi.kakao.com/v2")
public interface KakaoUserInfoFeignClient {
    @GetMapping("/user/me")
    KaKaoUserInfoRes getUserInfo (@RequestHeader("Authorization") String accessToken,
                                  @RequestParam("property_keys") String properties);
}
