package com.econo_4factorial.newproject.common;

import com.econo_4factorial.newproject.common.exception.RedisNotReadyException;
import com.econo_4factorial.newproject.user.service.RandomNicknamePoolManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NicknamePoolInitializerTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private RandomNicknamePoolManager poolManager;

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @Mock
    private RedisConnection redisConnection;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private NicknamePoolInitializer nicknamePoolInitializer;

    @BeforeEach
    void setUp() {
        nicknamePoolInitializer = new NicknamePoolInitializer(redisTemplate, poolManager);
    }

    @Test
    void 레디스가_준비되면_예외없이_통과한다() {
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.ping()).thenReturn("pong");

        assertThatCode(() -> nicknamePoolInitializer.waitForRedisReady())
                .doesNotThrowAnyException();
    }

    @Test
    void 애플리케이션_기동시_닉네임풀이_없으면_시작포지션을_초기화하고_풀을_관리한다() {
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.ping()).thenReturn("pong");
        when(redisTemplate.hasKey("users:randomNickname:pool:start")).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        nicknamePoolInitializer.onApplicationReady();

        verify(valueOperations).append("users:randomNickname:pool:start", "1");
        verify(poolManager).manageRandomNicknameSuffixPool();
    }

    @Test
    void 애플리케이션_기동시_닉네임풀이_있으면_초기화는_건너뛰고_풀만_관리한다() {
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.ping()).thenReturn("pong");
        when(redisTemplate.hasKey("users:randomNickname:pool:start")).thenReturn(true);

        nicknamePoolInitializer.onApplicationReady();

        verify(redisTemplate, never()).opsForValue();
        verify(poolManager).manageRandomNicknameSuffixPool();
    }

    @Test
    void 재시도_복구에_실패하면_레디스_준비예외를_던진다() {
        assertThatThrownBy(() -> nicknamePoolInitializer.recover(new RuntimeException("fail")))
                .isInstanceOf(RedisNotReadyException.class);
    }
}
