package com.econo_4factorial.newproject.base.service.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherScheduler {

    private final WeatherService weatherService;

    @Scheduled(cron = "${weather.scheduler.cron}", zone = "Asia/Seoul")
    public void run() {
        log.info("Weather update started");
        weatherService.updateAllBaseWeather();
        log.info("Weather update finished");
    }

    @EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
    public void runOnceOnStartup() {
        log.info("Initial Weather update started");
        weatherService.updateAllBaseWeather();
        log.info("Initial Weather update finished");
    }
}
