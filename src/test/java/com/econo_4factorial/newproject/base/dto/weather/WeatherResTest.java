package com.econo_4factorial.newproject.base.dto.weather;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherResTest {

    @Test
    void 날씨_응답을_DTO로_변환한다() {
        WeatherRes weatherRes = new WeatherRes(
                new WeatherRes.WeatherArray[]{
                        new WeatherRes.WeatherArray("Rain"),
                        new WeatherRes.WeatherArray("Clouds")
                },
                new WeatherRes.Main(293.15)
        );

        WeatherDTO result = weatherRes.toDTO();

        assertThat(result.weather()).isEqualTo("Rain");
        assertThat(result.temperature()).isEqualTo(293.15);
    }
}
