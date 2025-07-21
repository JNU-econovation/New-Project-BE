package com.econo_4factorial.newproject.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String token = request.getHeader("Authorization");
        System.out.println("[INTERCEPTOR] URI: " + uri);
        System.out.println("[INTERCEPTOR] Authorization Header: " + token);


        if (token ==null || !token.startsWith("Bearer")) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("[INTERCEPTOR] ❌ 토큰이 없거나 Bearer 형식이 아님 - 401 반환");
            return false;
        }

        return true;
    }
}
