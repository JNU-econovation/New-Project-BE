package com.econo_4factorial.newproject.auth.service.apple;

import com.econo_4factorial.newproject.auth.dto.apple.ApplePublicKeysResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleOAuthFeinghClient", url = "https://appleid.apple.com/auth")
public interface AppleOAuthFeignClient {
    @GetMapping("/keys")
    ApplePublicKeysResponse getApplePublicKeys();
}
