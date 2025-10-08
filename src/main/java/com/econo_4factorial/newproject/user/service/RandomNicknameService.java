package com.econo_4factorial.newproject.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomNicknameService {
    private static final String BASE_NICKNAME = "무등산";

    private final RandomNicknamePoolService nicknameQueue;

    public String getRandomNickname() {
        return BASE_NICKNAME + nicknameQueue.pop();
    }
}
