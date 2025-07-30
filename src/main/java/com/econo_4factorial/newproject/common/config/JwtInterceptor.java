package com.econo_4factorial.newproject.common.config;

import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
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
    private final AuthTokenService authTokenService;

    public JwtInterceptor(JwtTokenProvider jwtTokenProvider, AuthTokenService authTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authTokenService = authTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if(isPreFlightRequest(request)) {
            return true;
        }

        if(isReissueRequest(request)) {
            return validateRefreshToken(request);
        }

        return !isLoggedOutRequest(request);
    }

    private boolean isPreFlightRequest(HttpServletRequest request) {
        return CorsUtils.isPreFlightRequest(request);
    }

    private boolean isReissueRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith(REISSUE_URI);
    }

    private boolean validateRefreshToken(HttpServletRequest request) {
        String token = extractToken(request);
        return jwtTokenProvider.validateRefreshToken(token);
    }

    private boolean isLoggedOutRequest(HttpServletRequest request) {
        Long userId = getUserIdFromAccessToken(request);
        return authTokenService.hasActiveRefreshToken(userId);
    }

    private String extractToken(HttpServletRequest request) {
        return jwtTokenProvider.extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
    }

    private Long getUserIdFromAccessToken(HttpServletRequest request) {
        return jwtTokenProvider.getUserIdFromAccessToken(extractToken(request));
    }
}
