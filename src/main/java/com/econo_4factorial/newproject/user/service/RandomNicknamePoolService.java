package com.econo_4factorial.newproject.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RandomNicknamePoolService {
    private static final String NICKNAME_POOL_KEY = "users:randomNickname:pool";

    private final StringRedisTemplate redisTemplate;

    public String pop() {
        return redisTemplate.opsForSet().pop(NICKNAME_POOL_KEY);
    }

    public Long getPoolSize() {
        Long nicknameSuffixCount = redisTemplate.opsForSet().size(NICKNAME_POOL_KEY);
        log.info("현재 랜덤 닉네임 숫자 갯수: {}", nicknameSuffixCount);
        return nicknameSuffixCount;
    }

    public void addNewSuffixes(String[] suffixes, Long start, Long end) {
        redisTemplate.opsForSet().add(NICKNAME_POOL_KEY, suffixes);
        log.info("랜덤 닉네임 숫자 추가 완료. 범위: {} ~ {}. 추가한 숫자 갯수: {}. 현재 숫자 갯수: {} ", start, end, suffixes.length, getPoolSize());
    }
}
