package com.econo_4factorial.newproject.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RandomNicknamePoolScheduler {
    private final RandomNicknamePoolManager poolManager;

    @Scheduled(cron = "${nickname.scheduler.cron}", zone = "Asia/Seoul")
    public void managePool() {
        poolManager.manageRandomNicknameSuffixPool();
    }
}
