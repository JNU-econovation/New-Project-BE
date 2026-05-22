package com.econo_4factorial.newproject.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI OpenAPI(ServletContext servletContext) {

        Server server = new Server().url(servletContext.getContextPath());

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("access-token");

        return new OpenAPI()
                .servers(List.of(server))
                .components(authSetting())
                .addSecurityItem(securityRequirement)
                .info(swaggerInfo());

    }

    private Info swaggerInfo() {
        License license = new License();
        license.setUrl("https://github.com/JNU-econovation/New-Project-BE.git");
        license.setName("산결 레포지토리");

        return new Info()
                .version("v1.0.0")
                .title("San-Gyeol API")
                .description("산결 API 문서입니다.")
                .license(license);
    }

    private Components authSetting() {
        return new Components()
                .addSecuritySchemes(
                        "access-token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .in(SecurityScheme.In.HEADER)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .name("Authorization")
                );
    }
}
