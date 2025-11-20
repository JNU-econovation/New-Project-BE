package com.econo_4factorial.newproject.common.config;

import com.econo_4factorial.newproject.common.annotation.resolver.UserIdResolver;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
                        "http://172.30.1.15:3000", // KT Giga Wifi
                        "http://192.168.0.27:3000", // Econo 5G
                        "http://192.168.0.7:3000", // dding
                        "http://10.20.10.176:3000", // JNU
                        "http://10.20.10.217:3000", // JNU
                        "http://10.30.131.71.3000", // JNU
                        "http://192.168.0.240:3000", // JNU
                        "http://192.168.0.21:3000",  // JNU
                        "https://soop.euichan.com", // 프론트 배포 주소
                        "https://econo.soop.euichan.com:17779",// 동방
                        "http://192.168.0.100:3000", // Econo 5G 라즈베리파이 프론트
                        "https://soop.euichan.com/" // 프론트 배포 주소
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
                        "/api/v1/oauth/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**");
    }

}
