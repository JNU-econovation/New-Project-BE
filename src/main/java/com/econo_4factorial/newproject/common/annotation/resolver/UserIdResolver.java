package com.econo_4factorial.newproject.common.annotation.resolver;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.NotExistTokenException;
import com.econo_4factorial.newproject.auth.jwt.service.JwtTokenProvider;
import com.econo_4factorial.newproject.common.annotation.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserIdResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) throw new NotExistTokenException();

        String token = jwtTokenProvider.extractToken(header);
        return jwtTokenProvider.getUserIdFromAccessToken(token);
    }
}
