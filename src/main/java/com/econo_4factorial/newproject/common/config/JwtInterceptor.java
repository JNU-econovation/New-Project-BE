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

        if(CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        String token = jwtTokenProvider.extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (request.getRequestURI().startsWith(REISSUE_URI)) {
            return jwtTokenProvider.validateRefreshToken(token);
        }


        String uri = request.getRequestURI();
        System.out.println("[INTERCEPTOR] URI: " + uri);
        System.out.println("[INTERCEPTOR] Authorization: " + token);

        return false;
    }
}
