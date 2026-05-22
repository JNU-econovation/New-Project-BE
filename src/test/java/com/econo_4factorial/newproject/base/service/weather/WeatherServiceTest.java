package com.econo_4factorial.newproject.base.service.weather;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.base.dto.weather.WeatherRes;
import com.econo_4factorial.newproject.base.repository.BaseRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherFeignClient weatherFeignClient;

    @Mock
    private BaseRepository baseRepository;

    @InjectMocks
    private WeatherService weatherService;

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-key");
    }

    @Test
    void 모든_베이스의_날씨를_업데이트한다() {
        Base base = mock(Base.class);
        Point point = geometryFactory.createPoint(new Coordinate(126.0, 35.0));
        WeatherRes weatherRes = new WeatherRes(
                new WeatherRes.WeatherArray[]{new WeatherRes.WeatherArray("Rain")},
                new WeatherRes.Main(293.15)
        );
        given(baseRepository.findAll()).willReturn(List.of(base));
        given(base.getGeoPoint()).willReturn(point);
        given(base.getAltitude()).willReturn(400L);
        given(weatherFeignClient.getWeather(BigDecimal.valueOf(35.0), BigDecimal.valueOf(126.0), "test-key"))
                .willReturn(weatherRes);

        weatherService.updateAllBaseWeather();

        verify(base).updateWeather("Rain", 18.0);
    }

    @Test
    void 날씨_응답이_없으면_업데이트하지_않는다() {
        Base base = mock(Base.class);
        Point point = geometryFactory.createPoint(new Coordinate(127.0, 36.0));
        given(baseRepository.findAll()).willReturn(List.of(base));
        given(base.getId()).willReturn(2L);
        given(base.getGeoPoint()).willReturn(point);
        given(base.getAltitude()).willReturn(100L);
        given(weatherFeignClient.getWeather(BigDecimal.valueOf(36.0), BigDecimal.valueOf(127.0), "test-key"))
                .willReturn(null);

        weatherService.updateAllBaseWeather();

        verify(base, never()).updateWeather(anyString(), anyDouble());
    }

    @Test
    void 날씨나_온도가_없으면_업데이트하지_않는다() {
        Base base = mock(Base.class);
        Point point = geometryFactory.createPoint(new Coordinate(128.0, 37.0));
        WeatherRes weatherRes = new WeatherRes(
                new WeatherRes.WeatherArray[]{new WeatherRes.WeatherArray(null)},
                new WeatherRes.Main(295.15)
        );
        given(baseRepository.findAll()).willReturn(List.of(base));
        given(base.getId()).willReturn(3L);
        given(base.getGeoPoint()).willReturn(point);
        given(base.getAltitude()).willReturn(200L);
        given(weatherFeignClient.getWeather(BigDecimal.valueOf(37.0), BigDecimal.valueOf(128.0), "test-key"))
                .willReturn(weatherRes);

        weatherService.updateAllBaseWeather();

        verify(base, never()).updateWeather(anyString(), anyDouble());
    }

    @Test
    void 온도_정보가_없으면_업데이트하지_않는다() {
        Base base = mock(Base.class);
        Point point = geometryFactory.createPoint(new Coordinate(128.5, 37.5));
        WeatherRes weatherRes = new WeatherRes(
                new WeatherRes.WeatherArray[]{new WeatherRes.WeatherArray("Clear")},
                new WeatherRes.Main(null)
        );
        given(baseRepository.findAll()).willReturn(List.of(base));
        given(base.getId()).willReturn(33L);
        given(base.getGeoPoint()).willReturn(point);
        given(base.getAltitude()).willReturn(250L);
        given(weatherFeignClient.getWeather(BigDecimal.valueOf(37.5), BigDecimal.valueOf(128.5), "test-key"))
                .willReturn(weatherRes);

        weatherService.updateAllBaseWeather();

        verify(base, never()).updateWeather(anyString(), anyDouble());
    }

    @Test
    void 한_베이스에서_예외가_나도_다음_베이스는_계속_처리한다() {
        Base firstBase = mock(Base.class);
        Base secondBase = mock(Base.class);
        Point firstPoint = geometryFactory.createPoint(new Coordinate(129.0, 38.0));
        Point secondPoint = geometryFactory.createPoint(new Coordinate(130.0, 39.0));
        WeatherRes weatherRes = new WeatherRes(
                new WeatherRes.WeatherArray[]{new WeatherRes.WeatherArray("Clouds")},
                new WeatherRes.Main(300.15)
        );
        given(baseRepository.findAll()).willReturn(List.of(firstBase, secondBase));
        given(firstBase.getId()).willReturn(4L);
        given(firstBase.getGeoPoint()).willReturn(firstPoint);
        given(firstBase.getAltitude()).willReturn(0L);
        given(secondBase.getGeoPoint()).willReturn(secondPoint);
        given(secondBase.getAltitude()).willReturn(0L);
        given(weatherFeignClient.getWeather(BigDecimal.valueOf(38.0), BigDecimal.valueOf(129.0), "test-key"))
                .willThrow(new RuntimeException("boom"));
        given(weatherFeignClient.getWeather(BigDecimal.valueOf(39.0), BigDecimal.valueOf(130.0), "test-key"))
                .willReturn(weatherRes);

        weatherService.updateAllBaseWeather();

        verify(secondBase).updateWeather("Clouds", 27.0);
    }
}
