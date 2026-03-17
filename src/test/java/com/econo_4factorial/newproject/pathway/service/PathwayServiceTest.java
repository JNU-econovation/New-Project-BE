package com.econo_4factorial.newproject.pathway.service;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import com.econo_4factorial.newproject.pathway.domain.Pathway;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.mapper.PathwayMapper;
import com.econo_4factorial.newproject.pathway.repository.PathwayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PathwayServiceTest {

    @Mock
    private PathwayRepository pathwayRepository;

    @Mock
    private CoursePathwaySequenceService coursePathwaySequenceService;

    @Mock
    private PathwayMapper pathwayMapper;

    private PathwayService pathwayService;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        pathwayService = new PathwayService(pathwayRepository, coursePathwaySequenceService, pathwayMapper);
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 코스_ID로_등산로목록을_조회한다() {
        CoursePathwaySequence first = org.mockito.Mockito.mock(CoursePathwaySequence.class);
        CoursePathwaySequence second = org.mockito.Mockito.mock(CoursePathwaySequence.class);
        Pathway firstPathway = org.mockito.Mockito.mock(Pathway.class);
        Pathway secondPathway = org.mockito.Mockito.mock(Pathway.class);
        PathwayDTO firstDto = PathwayDTO.builder()
                .pathwayId(1L)
                .deptBaseId(10L)
                .destBaseId(11L)
                .difficulty(Difficulty.EASY)
                .coordinates(라인을_생성한다(new Coordinate(126.0, 37.0), new Coordinate(126.1, 37.1)))
                .build();
        PathwayDTO secondDto = PathwayDTO.builder()
                .pathwayId(2L)
                .deptBaseId(11L)
                .destBaseId(12L)
                .difficulty(Difficulty.NORMAL)
                .coordinates(라인을_생성한다(new Coordinate(126.2, 37.2), new Coordinate(126.3, 37.3)))
                .build();
        given(coursePathwaySequenceService.findByCourseId(5L)).willReturn(List.of(first, second));
        given(first.getPathway()).willReturn(firstPathway);
        given(second.getPathway()).willReturn(secondPathway);
        given(pathwayMapper.toDTO(firstPathway)).willReturn(firstDto);
        given(pathwayMapper.toDTO(secondPathway)).willReturn(secondDto);

        List<PathwayDTO> result = pathwayService.getPathwaysByCourseId(5L);

        assertThat(result).containsExactly(firstDto, secondDto);
        verify(coursePathwaySequenceService).findByCourseId(5L);
        verify(pathwayMapper).toDTO(firstPathway);
        verify(pathwayMapper).toDTO(secondPathway);
    }

    @Test
    void 코스에_등산로가_없으면_빈목록을_반환한다() {
        given(coursePathwaySequenceService.findByCourseId(6L)).willReturn(List.of());

        List<PathwayDTO> result = pathwayService.getPathwaysByCourseId(6L);

        assertThat(result).isEmpty();
    }

    private LineString 라인을_생성한다(Coordinate... coordinates) {
        return geometryFactory.createLineString(coordinates);
    }
}
