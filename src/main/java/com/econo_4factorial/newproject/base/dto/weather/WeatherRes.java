package com.econo_4factorial.newproject.base.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherRes(
        @JsonProperty("weather")
        Weather [] weather,
        @JsonProperty("main")
        Main main
) {
    public record Weather(
            @JsonProperty("main")
            String main
    ){
        public Weather (String main) {
            this.main = main;
        }
    }

    public record Main(
            @JsonProperty("temp")
            Double temp
    ){
        public Main (Double temp) {
            this.temp = temp;
        }
    }

    public WeatherDTO toDTO() {
        return new WeatherDTO(this.weather[0].main, this.main().temp);
    }
}
