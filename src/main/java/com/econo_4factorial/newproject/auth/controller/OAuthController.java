package com.econo_4factorial.newproject.auth.controller;

import com.econo_4factorial.newproject.auth.dto.Res.KakaoUriRes;
import com.econo_4factorial.newproject.auth.dto.Req.AppleLoginReq;
import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import com.econo_4factorial.newproject.auth.service.OAuthService;
import com.econo_4factorial.newproject.common.util.HttpHeadersGenerator;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/kakao/login")
    public ApiResult<ApiResult.SuccessBody<KakaoUriRes>> getKakaoLoginUri() {
        String uri = oAuthService.getKakaoLoginURI();
        return ApiResponse.success(new KakaoUriRes(uri), HttpStatus.OK);
    }

    @GetMapping("/kakao/callback")
    public ApiResult<ApiResult.SuccessBody<Void>> loginWithKakao (@RequestParam("code") String kakaoAuthorizationCode) {
        AuthToken authToken = oAuthService.loginWithKaKao(kakaoAuthorizationCode);
        HttpHeaders headers = HttpHeadersGenerator.setLocation(RedirectUriBuilder.buildLoginSuccessUri(authToken));
        return ApiResponse.success(headers, HttpStatus.FOUND);
    }

    @PostMapping("/apple/login")
    public ApiResult<ApiResult.SuccessBody<Void>> loginWithApple (@RequestBody @Valid AppleLoginReq appleLoginReq) {
        AuthToken authToken = oAuthService.loginWithApple(appleLoginReq);
        HttpHeaders headers = HttpHeadersGenerator.setLocation(RedirectUriBuilder.buildLoginSuccessUri(authToken));
        return ApiResponse.success(headers, HttpStatus.FOUND);
    }
}
