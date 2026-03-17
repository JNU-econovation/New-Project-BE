package com.econo_4factorial.newproject.base.service.weather;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WeatherSchedulerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherScheduler weatherScheduler;

    @Test
    void 정기_날씨갱신을_실행한다() {
        weatherScheduler.run();

        verify(weatherService).updateAllBaseWeather();
    }

    @Test
    void 애플리케이션_시작시_한번_날씨갱신을_실행한다() {
        weatherScheduler.runOnceOnStartup();

        verify(weatherService).updateAllBaseWeather();
    }
}
