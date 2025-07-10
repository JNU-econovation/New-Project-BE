package com.econo_4factorial.newproject.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
                        "http://192.168.0.240:3000", // JNU
                        "https://soop.euichan.com/" // 프론트 배포 주소
                        )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
