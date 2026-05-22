package com.econo_4factorial.newproject.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RandomNicknameServiceTest {

    @Mock
    private RandomNicknamePoolService randomNicknamePoolService;

    private RandomNicknameService randomNicknameService;

    @BeforeEach
    void setUp() {
        randomNicknameService = new RandomNicknameService(randomNicknamePoolService);
    }

    @Test
    void 기본_접두사와_랜덤_접미사를_합쳐서_닉네임을_반환한다() {
        given(randomNicknamePoolService.pop()).willReturn("1234");

        String nickname = randomNicknameService.getRandomNickname();

        assertThat(nickname).isEqualTo("무등산1234");
    }
}
