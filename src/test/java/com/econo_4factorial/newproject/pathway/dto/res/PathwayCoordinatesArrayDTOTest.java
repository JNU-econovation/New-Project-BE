package com.econo_4factorial.newproject.pathway.dto.res;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;

class PathwayCoordinatesArrayDTOTest {

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 등산로_좌표응답을_생성한다() {
        LineString coordinates = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(126.0, 37.0),
                new Coordinate(126.1, 37.1)
        });
        PathwayDTO pathwayDTO = PathwayDTO.builder()
                .pathwayId(3L)
                .deptBaseId(4L)
                .destBaseId(5L)
                .difficulty(Difficulty.EASY)
                .coordinates(coordinates)
                .build();

        PathwayCoordinatesArrayDTO result = PathwayCoordinatesArrayDTO.from(pathwayDTO);

        assertThat(result.pathwayId()).isEqualTo(3L);
        assertThat(result.deptBaseId()).isEqualTo(4L);
        assertThat(result.destBaseId()).isEqualTo(5L);
        assertThat(result.difficulty()).isEqualTo(Difficulty.EASY);
        assertThat(result.coordinates()).hasSize(2);
        assertThat(result.coordinates().get(0)).containsExactly(
                BigDecimal.valueOf(126.0),
                BigDecimal.valueOf(37.0)
        );
    }
}
