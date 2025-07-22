package com.econo_4factorial.newproject.common.config;

import com.econo_4factorial.newproject.auth.jwt.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final String REISSUE_URI = "/api/v1/oauth/reissue";
    private final JwtTokenProvider jwtTokenProvider;

    public JwtInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // CORS Preflight 요청은 토큰 검증 X
        if(CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        String token = jwtTokenProvider.extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        System.out.println(request.getHeader(HttpHeaders.AUTHORIZATION));
        System.out.println(token);

        // 액세스토큰 재발급 요청에 대한 리프레시 토큰검증
        if (request.getRequestURI().startsWith(REISSUE_URI)) {
            return jwtTokenProvider.validateRefreshToken(token);
        }

        Long userId = jwtTokenProvider.getUserIdFromAccessToken(token);
        System.out.println(userId);

        // 로그아웃된 사용자의 요청 차단
        return jwtTokenProvider.existByUserIdOrThrow(userId);
    }
}
