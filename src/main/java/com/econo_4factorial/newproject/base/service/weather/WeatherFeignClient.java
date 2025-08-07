package com.econo_4factorial.newproject.base.service.weather;

import com.econo_4factorial.newproject.base.dto.weather.WeatherRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherClient", url = "https://api.openweathermap.org/data/2.5")
public interface WeatherFeignClient {

    @GetMapping("/weather")
    WeatherRes getWeather(
            @RequestParam("lat") Double latitude,
            @RequestParam("lon") Double longitude,
            @RequestParam("appid") String apikey
    );
}
