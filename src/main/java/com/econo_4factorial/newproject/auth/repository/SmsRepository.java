package com.econo_4factorial.newproject.auth.repository;

import com.econo_4factorial.newproject.common.config.SmsProperties;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SmsRepository {
    private final SmsProperties smsProperties;
    private final StringRedisTemplate redisTemplate;

    static final String REDIS_PREFIX = "sms:";

    public void createSmsVerification(String phoneNumber, String verificationCode) {
        redisTemplate.opsForValue()
                .set(REDIS_PREFIX + phoneNumber, verificationCode, Duration.ofSeconds(smsProperties.getTtl()));
    }

    public String getSmsVerification(String phoneNumber) {
        return redisTemplate.opsForValue().get(REDIS_PREFIX + phoneNumber);
    }

    public void deleteSmsVerification(String phoneNumber) {
        redisTemplate.delete(REDIS_PREFIX + phoneNumber);
    }
}
