package com.econo_4factorial.newproject.auth.controller;

import com.econo_4factorial.newproject.auth.dto.Res.LoginRes;
import com.econo_4factorial.newproject.auth.service.OAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("kakao")
    public ResponseEntity<Void> getKakaoLoginUri() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(oAuthService.getKakaoLoginURI()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }

    @GetMapping("/kakao-redirect")
    public ResponseEntity<LoginRes> loginWithKakao (@RequestParam("code") String kakaoAuthorizationCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("")); //추후 프론트 주소로 수정 필요
        LoginRes authToken = oAuthService.loginWithKaKao(kakaoAuthorizationCode);
        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }
}
