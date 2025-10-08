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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherFeignClient weatherFeignClient;
    private final BaseRepository baseRepository;
    static final double KELVIN_TO_CELSIUS_OFFSET = 273.15; // [°C]
    static final double ISA_LAPSE_RATE_C_PER_M   = 0.0065; // [°C/m] (표준대기 감률: 6.5°C/km)

    @Value("${weather.api.key}")
    private String apiKey;

    @Transactional
    public void updateAllBaseWeather() {
        List<Base> bases = baseRepository.findAll();
        for (Base base : bases) {
            try {
                BigDecimal lat = BigDecimal.valueOf(base.getGeoPoint().getCoordinate().getY());
                BigDecimal lon = BigDecimal.valueOf(base.getGeoPoint().getCoordinate().getX());
                Long altitude = base.getAltitude();

                Optional<WeatherRes> weatherOpt = Optional.ofNullable(weatherFeignClient.getWeather(lat, lon, apiKey));

                if (weatherOpt.isEmpty()) {
                    log.warn("No response, baseId={}", base.getId());
                    continue;
                }

                WeatherRes weatherRes = weatherOpt.get();

                WeatherDTO WeatherDTO = weatherRes.toDTO();
                Double kelvinObj = WeatherDTO.temperature();
                String weather = WeatherDTO.weather();

                if (kelvinObj == null || weather == null) {
                    if (kelvinObj == null) {
                        log.warn("No temperature for baseId={}", base.getId());
                    }
                    if (weather == null) {
                        log.warn("No weather for baseId={}", base.getId());
                    }
                    continue;
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
        return kelvin - KELVIN_TO_CELSIUS_OFFSET;
    }

    private double applyAltitudeCorrection(double temperature, long altitude) {
        return Math.ceil(temperature - (altitude * ISA_LAPSE_RATE_C_PER_M));
    }

}
