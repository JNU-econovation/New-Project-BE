package com.econo_4factorial.newproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients
public class NewProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewProjectApplication.class, args);
    }

}
