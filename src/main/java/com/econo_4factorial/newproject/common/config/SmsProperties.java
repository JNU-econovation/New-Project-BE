package com.econo_4factorial.newproject.common.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "coolsms.api")
@Validated
public class SmsProperties {
    @NotBlank
    private String key;
    @NotBlank
    private String secret;
    @NotBlank
    private String senderNumber;
    private String smsPrefix;
    @Positive
    private int ttl;
}
