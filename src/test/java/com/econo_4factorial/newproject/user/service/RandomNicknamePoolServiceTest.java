package com.econo_4factorial.newproject.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@ExtendWith(MockitoExtension.class)
class RandomNicknamePoolServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private SetOperations<String, String> setOperations;

    private RandomNicknamePoolService randomNicknamePoolService;

    @BeforeEach
    void setUp() {
        randomNicknamePoolService = new RandomNicknamePoolService(redisTemplate);
    }

    @Test
    void 랜덤_닉네임_접미사를_하나_꺼낸다() {
        given(redisTemplate.opsForSet()).willReturn(setOperations);
        given(setOperations.pop("users:randomNickname:pool")).willReturn("1234");

        String suffix = randomNicknamePoolService.pop();

        assertThat(suffix).isEqualTo("1234");
    }

    @Test
    void 풀_사이즈를_조회한다() {
        given(redisTemplate.opsForSet()).willReturn(setOperations);
        given(setOperations.size("users:randomNickname:pool")).willReturn(321L);

        Long poolSize = randomNicknamePoolService.getPoolSize();

        assertThat(poolSize).isEqualTo(321L);
    }

    @Test
    void 새로운_접미사들을_추가한다() {
        String[] suffixes = {"0001", "0002"};
        given(redisTemplate.opsForSet()).willReturn(setOperations);
        given(setOperations.size("users:randomNickname:pool")).willReturn(2L);

        randomNicknamePoolService.addNewSuffixes(suffixes, 1L, 2L);

        verify(setOperations).add("users:randomNickname:pool", suffixes);
    }
}
