package com.econo_4factorial.newproject.auth.controller;

import com.econo_4factorial.newproject.auth.dto.Res.AppleLoginRes;
import com.econo_4factorial.newproject.auth.dto.Res.KakaoUriRes;
import com.econo_4factorial.newproject.auth.dto.Req.AppleLoginReq;
import com.econo_4factorial.newproject.auth.jwt.AuthToken;
import com.econo_4factorial.newproject.auth.service.OAuthService;
import com.econo_4factorial.newproject.common.util.HttpHeadersGenerator;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
@Tag(name = "Auth", description = "인증 API")
public class OAuthController {
    private final OAuthService oAuthService;
    private final RedirectUriBuilder redirectUriBuilder;

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인 URI 요청", description = "프론트에서 카카오 로그인 페이지로 리다이렉트하기 위한 URI를 반환합니다.")
    public ApiResult<ApiResult.SuccessBody<KakaoUriRes>> getKakaoLoginUri() {
        String uri = oAuthService.getKakaoLoginURI();
        return ApiResponse.success(new KakaoUriRes(uri), HttpStatus.OK);
    }

    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 로그인 콜백 처리", description = "카카오 로그인 후 리다이렉트된 코드로 사용자 인증을 처리합니다.")
    public ApiResult<ApiResult.SuccessBody<Void>> loginWithKakao (
            @Parameter(description = "카카오 인가 코드", example = "abc123", required = true)
            @RequestParam("code") String kakaoAuthorizationCode) {
        AuthToken authToken = oAuthService.loginWithKaKao(kakaoAuthorizationCode);
        HttpHeaders headers = HttpHeadersGenerator.setLocation(redirectUriBuilder.buildLoginSuccessUri(authToken));
        return ApiResponse.success(headers, HttpStatus.FOUND);
    }

    @PostMapping("/apple/login")
    @Operation(summary = "애플 로그인", description = "애플 ID 토큰을 기반으로 로그인 처리합니다.")
    public ApiResult<ApiResult.SuccessBody<AppleLoginRes>> loginWithApple (@RequestBody @Valid AppleLoginReq appleLoginReq) {
        AuthToken authToken = oAuthService.loginWithApple(appleLoginReq);
        return ApiResponse.success(AppleLoginRes.from(authToken), HttpStatus.CREATED);
    }
}
