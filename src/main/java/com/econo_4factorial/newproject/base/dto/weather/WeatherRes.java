package com.econo_4factorial.newproject.base.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherRes(
        @JsonProperty("weather")
        WeatherArray [] weatherArray,
        @JsonProperty("main")
        Main main
) {
    public record WeatherArray(
            @JsonProperty("main")
            String weather
    ){
        public WeatherArray(String weather) {
            this.weather = weather;
        }
    }

    public record Main(
            @JsonProperty("temp")
            Double temperature
    ){
        public Main (Double temperature) {
            this.temperature = temperature;
        }
    }

    public WeatherDTO toDTO() {
        return new WeatherDTO(this.weatherArray[0].weather, this.main().temperature);
    }
}
