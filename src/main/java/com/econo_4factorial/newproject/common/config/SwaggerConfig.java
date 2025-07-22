package com.econo_4factorial.newproject.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import jakarta.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@SecurityScheme(
//        name = "BearerAuth",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)
@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI OpenAPI(ServletContext servletContext) {

                return new OpenAPI()
                        .info(swaggerInfo());

        }

        private  Info swaggerInfo() {
                License license = new License();
                license.setUrl("https://github.com/JNU-econovation/New-Project-BE.git");
                license.setName("산결");

                return new Info()
                        .version("v1.0.0")
                        .title("San-Gyeol API")
                        .description("산결 API 문서입니다.")
                        .license(license);
        }
}
