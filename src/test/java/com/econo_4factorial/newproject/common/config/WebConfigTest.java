package com.econo_4factorial.newproject.common.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import com.econo_4factorial.newproject.common.annotation.resolver.UserIdResolver;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

class WebConfigTest {

    @Test
    void UserId_리졸버를_등록한다() {
        UserIdResolver userIdResolver = mock(UserIdResolver.class);
        JwtInterceptor jwtInterceptor = mock(JwtInterceptor.class);
        CorsProperties corsProperties = new CorsProperties();
        WebConfig webConfig = new WebConfig(userIdResolver, jwtInterceptor, corsProperties);
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        webConfig.addArgumentResolvers(resolvers);

        assertThat(resolvers).containsExactly(userIdResolver);
    }

    @Test
    void JWT_인터셉터를_API_경로에_등록하고_예외_경로를_제외한다() {
        UserIdResolver userIdResolver = mock(UserIdResolver.class);
        JwtInterceptor jwtInterceptor = mock(JwtInterceptor.class);
        CorsProperties corsProperties = new CorsProperties();
        WebConfig webConfig = new WebConfig(userIdResolver, jwtInterceptor, corsProperties);
        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        InterceptorRegistration registration = mock(InterceptorRegistration.class);

        given(registry.addInterceptor(jwtInterceptor)).willReturn(registration);
        given(registration.addPathPatterns("/api/**")).willReturn(registration);
        given(registration.excludePathPatterns(
                "/api/v1/oauth/kakao/login",
                "/api/v1/oauth/kakao/callback",
                "/api/v1/oauth/apple/login",
                "/api/v1/oauth/reissue",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**"
        )).willReturn(registration);

        webConfig.addInterceptors(registry);

        InOrder inOrder = inOrder(registry, registration);
        inOrder.verify(registry).addInterceptor(jwtInterceptor);
        inOrder.verify(registration).addPathPatterns("/api/**");
        inOrder.verify(registration).excludePathPatterns(
                "/api/v1/oauth/kakao/login",
                "/api/v1/oauth/kakao/callback",
                "/api/v1/oauth/apple/login",
                "/api/v1/oauth/reissue",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**"
        );
    }

    @Test
    void CORS_매핑을_등록한다() {
        UserIdResolver userIdResolver = mock(UserIdResolver.class);
        JwtInterceptor jwtInterceptor = mock(JwtInterceptor.class);
        CorsProperties corsProperties = new CorsProperties();
        corsProperties.setAllowedOrigins(List.of(
                "https://localhost:3000",
                "http://localhost:3000",
                "https://econo.soop.euichan.com",
                "https://api.econo.soop.euichan.com"
        ));
        WebConfig webConfig = new WebConfig(userIdResolver, jwtInterceptor, corsProperties);
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        given(registry.addMapping("/**")).willReturn(registration);
        given(registration.allowedOrigins(
                "https://localhost:3000",
                "http://localhost:3000",
                "https://econo.soop.euichan.com",
                "https://api.econo.soop.euichan.com"
        )).willReturn(registration);
        given(registration.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")).willReturn(registration);
        given(registration.allowedHeaders("*")).willReturn(registration);
        given(registration.allowCredentials(true)).willReturn(registration);

        webConfig.addCorsMappings(registry);

        InOrder inOrder = inOrder(registry, registration);
        inOrder.verify(registry).addMapping("/**");
        inOrder.verify(registration).allowedOrigins(
                "https://localhost:3000",
                "http://localhost:3000",
                "https://econo.soop.euichan.com",
                "https://api.econo.soop.euichan.com"
        );
        inOrder.verify(registration).allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
        inOrder.verify(registration).allowedHeaders("*");
        inOrder.verify(registration).allowCredentials(true);
    }
}
