package com.econo_4factorial.newproject.pathway.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.pathway.domain.Pathway;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;

class PathwayMapperTest {

    private final PathwayMapper pathwayMapper = new PathwayMapper();

    @Test
    void deptBaseId는_출발지_destBaseId는_도착지_base에서_가져온다() {
        Base departure = mock(Base.class);
        Base destination = mock(Base.class);
        when(departure.getId()).thenReturn(21L);
        when(destination.getId()).thenReturn(11L);

        LineString coordinates = mock(LineString.class);
        Pathway pathway = mock(Pathway.class);
        when(pathway.getId()).thenReturn(1L);
        when(pathway.getDeparture()).thenReturn(departure);
        when(pathway.getDestination()).thenReturn(destination);
        when(pathway.getDifficulty()).thenReturn(Difficulty.NORMAL);
        when(pathway.getCoordinates()).thenReturn(coordinates);

        PathwayDTO result = pathwayMapper.toDTO(pathway);

        assertThat(result.pathwayId()).isEqualTo(1L);
        assertThat(result.deptBaseId()).isEqualTo(21L);
        assertThat(result.destBaseId()).isEqualTo(11L);
        assertThat(result.difficulty()).isEqualTo(Difficulty.NORMAL);
        assertThat(result.coordinates()).isSameAs(coordinates);
    }
}
