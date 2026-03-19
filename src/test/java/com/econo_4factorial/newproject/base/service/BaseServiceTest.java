package com.econo_4factorial.newproject.base.service;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.base.domain.BaseImage;
import com.econo_4factorial.newproject.base.dto.BaseDTO;
import com.econo_4factorial.newproject.base.dto.BaseDetailDTO;
import com.econo_4factorial.newproject.base.repository.BaseImageRepository;
import com.econo_4factorial.newproject.base.repository.BaseRepository;
import com.econo_4factorial.newproject.mountain.exception.BadRequestException.MountainNotFoundException;
import com.econo_4factorial.newproject.mountain.service.MountainService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class BaseServiceTest {

    @Mock
    private BaseRepository baseRepository;

    @Mock
    private BaseImageRepository baseImageRepository;

    @Mock
    private MountainService mountainService;

    @InjectMocks
    private BaseService baseService;

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 산의_베이스목록을_조회한다() {
        Base base = mock(Base.class);
        Point point = geometryFactory.createPoint(new Coordinate(126.9876, 35.1234));
        given(base.getId()).willReturn(1L);
        given(base.getName()).willReturn("중머리재");
        given(base.getGeoPoint()).willReturn(point);
        given(baseRepository.findByMountainId(7L)).willReturn(List.of(base));

        List<BaseDTO> result = baseService.getBasesByMountainId(7L);

        assertThat(result).containsExactly(
                new BaseDTO(1L, "중머리재",
                        List.of(new java.math.BigDecimal("126.9876"), new java.math.BigDecimal("35.1234")))
        );
        verify(mountainService).isMountainExistOrThrow(7L);
    }

    @Test
    void 산의_베이스상세목록을_조회한다() {
        Base firstBase = mock(Base.class);
        Base secondBase = mock(Base.class);
        BaseImage firstImage = mock(BaseImage.class);
        BaseImage secondImage = mock(BaseImage.class);
        given(firstBase.getId()).willReturn(11L);
        given(firstBase.getName()).willReturn("장불재");
        given(firstBase.getWeather()).willReturn("Rain");
        given(firstBase.getTemperature()).willReturn(13.0);
        given(secondBase.getId()).willReturn(12L);
        given(secondBase.getName()).willReturn("입석대");
        given(secondBase.getWeather()).willReturn("Clouds");
        given(secondBase.getTemperature()).willReturn(15.0);
        given(firstImage.getImageUrl()).willReturn("/bases/11-1.png");
        given(secondImage.getImageUrl()).willReturn("/bases/12-1.png");
        given(baseRepository.findByMountainId(8L)).willReturn(List.of(firstBase, secondBase));
        given(baseImageRepository.findByBaseId(11L)).willReturn(List.of(firstImage));
        given(baseImageRepository.findByBaseId(12L)).willReturn(List.of(secondImage));

        List<BaseDetailDTO> result = baseService.getBaseDetailsByMountainId(8L);

        assertThat(result).containsExactly(
                new BaseDetailDTO(11L, "장불재", "Rain", 13.0, null, List.of("/bases/11-1.png")),
                new BaseDetailDTO(12L, "입석대", "Clouds", 15.0, null, List.of("/bases/12-1.png"))
        );
        verify(baseImageRepository).findByBaseId(11L);
        verify(baseImageRepository).findByBaseId(12L);
    }

    @Test
    void 없는_산의_베이스를_조회하면_예외가_발생한다() {
        MountainNotFoundException exception = new MountainNotFoundException();
        doThrow(exception).when(mountainService).isMountainExistOrThrow(99L);

        assertThatThrownBy(() -> baseService.getBasesByMountainId(99L))
                .isSameAs(exception);
        verifyNoInteractions(baseRepository, baseImageRepository);
    }
}
