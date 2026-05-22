package com.econo_4factorial.newproject.user.service;

import java.util.stream.LongStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RandomNicknamePoolManager {
    private static final String NICKNAME_POOL_START_KEY = "users:randomNickname:pool:start";
    private static final Long NICKNAME_POOL_THRESHOLD = 500L;
    private static final Long CHUNK_SIZE = 1999L;
    private static final String START_VALUE_NOT_FOUND_MESSAGE = "랜덤 닉네임 숫자 생성 과정에서 start 위치의 데이터가 존재하지 않습니다";

    private final StringRedisTemplate redisTemplate;
    private final RandomNicknamePoolService nicknamePool;

    public void manageRandomNicknameSuffixPool() {
        if (!hasEnoughSuffix()) {
            log.info("랜덤 닉네임 보충 시작");
            Long start = getStartPosition();
            Long end = start + CHUNK_SIZE;

            String[] suffixes = generateSuffixes(start, end);

            nicknamePool.addNewSuffixes(suffixes, start, end);

            updateStartPosition(end);
        }
    }

    private void updateStartPosition(Long end) {
        redisTemplate.opsForValue().set(NICKNAME_POOL_START_KEY, String.valueOf(end + 1));
    }

    private String[] generateSuffixes(Long start, Long end) {
        return LongStream.rangeClosed(start, end)
                .mapToObj(num -> String.format("%04d", num))
                .toArray(String[]::new);
    }

    private boolean hasEnoughSuffix() {
        return nicknamePool.getPoolSize() > NICKNAME_POOL_THRESHOLD;
    }

    private Long getStartPosition() {
        String startValue = redisTemplate.opsForValue().get(NICKNAME_POOL_START_KEY);
        if (startValue == null) {
            throw new IllegalStateException(START_VALUE_NOT_FOUND_MESSAGE);
        }
        return Long.valueOf(startValue);
    }
}
