package com.econo_4factorial.newproject.base.dto.weather;

public record GetWeatherRes(
        String weather,
        Double temperature
) {
    public static GetWeatherRes from(String weather, Double temperature) {
        return new GetWeatherRes(weather, temperature);
    }
}
