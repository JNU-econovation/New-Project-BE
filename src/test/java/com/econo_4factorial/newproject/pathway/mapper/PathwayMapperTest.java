package com.econo_4factorial.newproject.pathway.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.pathway.domain.Pathway;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mockito;

class PathwayMapperTest {

    private GeometryFactory geometryFactory;
    private PathwayMapper pathwayMapper;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        pathwayMapper = new PathwayMapper();
    }

    @Test
    void 등산로를_DTO로_변환한다() {
        Base departure = Mockito.mock(Base.class);
        Base destination = Mockito.mock(Base.class);
        Pathway pathway = Mockito.mock(Pathway.class);
        LineString coordinates = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(126.0, 37.0),
                new Coordinate(126.1, 37.1)
        });

        Mockito.when(pathway.getId()).thenReturn(31L);
        Mockito.when(pathway.getDeparture()).thenReturn(departure);
        Mockito.when(pathway.getDestination()).thenReturn(destination);
        Mockito.when(departure.getId()).thenReturn(1L);
        Mockito.when(destination.getId()).thenReturn(2L);
        Mockito.when(pathway.getDifficulty()).thenReturn(Difficulty.HARD);
        Mockito.when(pathway.getCoordinates()).thenReturn(coordinates);

        PathwayDTO result = pathwayMapper.toDTO(pathway);

        assertThat(result.pathwayId()).isEqualTo(31L);
        assertThat(result.deptBaseId()).isEqualTo(1L);
        assertThat(result.destBaseId()).isEqualTo(2L);
        assertThat(result.difficulty()).isEqualTo(Difficulty.HARD);
        assertThat(result.coordinates()).isEqualTo(coordinates);
    }
}
