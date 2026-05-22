package com.econo_4factorial.newproject.common;

import com.econo_4factorial.newproject.common.exception.RedisNotReadyException;
import com.econo_4factorial.newproject.user.service.RandomNicknamePoolManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableRetry
@ConditionalOnProperty(value = "app.startup-jobs.enabled", havingValue = "true", matchIfMissing = true)
public class NicknamePoolInitializer {
    private static final String NICKNAME_POOL_START_KEY = "users:randomNickname:pool:start";
    private static final String NICKNAME_POOL_START_INITIAL_VALUE = "1";
    private static final String PONG = "pong";

    private final StringRedisTemplate redisTemplate;
    private final RandomNicknamePoolManager poolManger;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        waitForRedisReady();
        initializeNicknamePoolIfNeeded();
        appendNicknamePoolIfNeeded();
    }

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    public void waitForRedisReady() {
        try {
            String response = redisTemplate.getConnectionFactory().getConnection().ping();
            log.info("Redis Ready");
        } catch (Exception e) {
            log.warn("Redis Not Ready. 재시도 실행...");
            throw e;
        }
    }

    @Recover
    public void recover(Exception e) {
        log.error("Redis Ready 상태 확인 실패.", e);
        throw new RedisNotReadyException();
    }

    private void initializeNicknamePoolIfNeeded() {
        if (!hasNicknamePool()) {
            initializeStartPosition();
            log.info("Redis. 랜덤 닉네임 START_POSITON_KEY 최초 값 1삽입.");
        }
    }

    private boolean hasNicknamePool() {
        return redisTemplate.hasKey(NICKNAME_POOL_START_KEY);
    }

    private void initializeStartPosition() {
        redisTemplate.opsForValue().append(NICKNAME_POOL_START_KEY, NICKNAME_POOL_START_INITIAL_VALUE);
    }

    private void appendNicknamePoolIfNeeded() {
        poolManger.manageRandomNicknameSuffixPool();
    }
}
