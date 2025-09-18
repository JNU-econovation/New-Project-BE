package com.econo_4factorial.newproject.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "coolsms.api")
public class SmsProperties {
    private String key;
    private String secret;
    private String senderNumber;
    private String prefix;
    private int ttl;
}
