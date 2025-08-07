package com.econo_4factorial.newproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")}) // 추가
public class NewProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewProjectApplication.class, args);
    }

}
