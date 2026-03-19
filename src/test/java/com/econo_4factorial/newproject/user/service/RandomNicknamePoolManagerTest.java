package com.econo_4factorial.newproject.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RandomNicknamePoolManagerTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private RandomNicknamePoolService randomNicknamePoolService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private RandomNicknamePoolManager randomNicknamePoolManager;

    @BeforeEach
    void setUp() {
        randomNicknamePoolManager = new RandomNicknamePoolManager(redisTemplate, randomNicknamePoolService);
    }

    @Test
    void 풀이_충분하면_보충하지_않는다() {
        given(randomNicknamePoolService.getPoolSize()).willReturn(501L);

        randomNicknamePoolManager.manageRandomNicknameSuffixPool();

        verify(randomNicknamePoolService, never()).addNewSuffixes(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.anyLong());
        verify(redisTemplate, never()).opsForValue();
    }

    @Test
    void 풀이_부족하면_시작위치부터_2000개의_접미사를_추가하고_다음_시작위치를_저장한다() {
        given(randomNicknamePoolService.getPoolSize()).willReturn(100L);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("users:randomNickname:pool:start")).willReturn("1");

        randomNicknamePoolManager.manageRandomNicknameSuffixPool();

        ArgumentCaptor<String[]> suffixCaptor = ArgumentCaptor.forClass(String[].class);
        verify(randomNicknamePoolService).addNewSuffixes(suffixCaptor.capture(), org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.eq(2000L));
        assertThat(suffixCaptor.getValue()).hasSize(2000);
        assertThat(suffixCaptor.getValue()[0]).isEqualTo("0001");
        assertThat(suffixCaptor.getValue()[1999]).isEqualTo("2000");
        verify(valueOperations).set("users:randomNickname:pool:start", "2001");
    }

    @Test
    void 시작위치가_없으면_예외가_발생한다() {
        given(randomNicknamePoolService.getPoolSize()).willReturn(100L);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("users:randomNickname:pool:start")).willReturn(null);

        assertThatThrownBy(() -> randomNicknamePoolManager.manageRandomNicknameSuffixPool())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("랜덤 닉네임 숫자 생성 과정에서 start 위치의 데이터가 존재하지 않습니다");
    }
}
