package com.econo_4factorial.newproject.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "San-Gyeol API",
                version = "v1",
                description = "산결 API 문서"
        )
)
@Configuration
public class SwaggerConfig {
}
