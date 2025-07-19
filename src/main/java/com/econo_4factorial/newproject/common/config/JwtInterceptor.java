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
        System.out.println("uri: " + uri);

        String token = request.getHeader("Authorization");
        System.out.println("token: " + token);

        if (token ==null || !token.startsWith("Bearer")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return true;
    }
}
