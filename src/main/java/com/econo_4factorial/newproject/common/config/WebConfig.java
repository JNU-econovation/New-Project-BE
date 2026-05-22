package com.econo_4factorial.newproject.common.config;

import com.econo_4factorial.newproject.common.annotation.resolver.UserIdResolver;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserIdResolver userIdResolver;
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://localhost:3000", // local
                        "http://localhost:3000",
                        "https://econo.soop.euichan.com", // Frontend
                        "https://api.econo.soop.euichan.com" // Backend
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/v1/oauth/kakao/login",
                        "/api/v1/oauth/kakao/callback",
                        "/api/v1/oauth/apple/login",
                        "/api/v1/oauth/reissue",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**");
    }

}
