package com.econo_4factorial.newproject.base.service.weather;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.base.dto.weather.WeatherDTO;
import com.econo_4factorial.newproject.base.dto.weather.WeatherRes;
import com.econo_4factorial.newproject.base.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherFeignClient weatherFeignClient;
    private final BaseRepository baseRepository;

    @Value("${weather.api.key}")
    private String apiKey;

    @Transactional
    public void updateAllBaseWeather() {
        List<Base> bases = baseRepository.findAll();
        for (Base base : bases) {
            try {
                BigDecimal lat = base.getLatitude();
                BigDecimal lon = base.getLongitude();
                Long altitude = base.getAltitude();

                WeatherRes weatherRes = weatherFeignClient.getWeather(lat, lon, apiKey);
                if (weatherRes == null) {
                    log.warn("No response, baseId={}", base.getId());
                    continue;
                }

                WeatherDTO WeatherDTO = weatherRes.toDTO();
                Double kelvinObj = WeatherDTO.temperature();
                String weather = WeatherDTO.weather();

                if (kelvinObj == null) {
                    log.warn("No temperature for baseId={}", base.getId());
                    continue;
                }

                if (weather == null) {
                    log.warn("No weather for baseId={}", base.getId());
                }

                double celsius = kelvinToCelsius(kelvinObj);
                double correctedTemperature = applyAltitudeCorrection(celsius, altitude);

                base.updateWeather(weather, correctedTemperature);
            } catch (Exception e) {
                log.error("Failed to update weather for baseId={}", base.getId(), e);
            }
        }
    }

    private double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    private double applyAltitudeCorrection(double temperature, long altitude) {
        return Math.ceil(temperature - (altitude * 0.0065));
    }

}
