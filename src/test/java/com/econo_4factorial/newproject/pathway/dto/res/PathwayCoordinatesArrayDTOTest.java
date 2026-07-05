package com.econo_4factorial.newproject.pathway.dto.res;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

class PathwayCoordinatesArrayDTOTest {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    // JTS 관례(x=경도, y=위도)로 점 2개짜리 LineString을 만든다.
    private static LineString lineString() {
        return GEOMETRY_FACTORY.createLineString(new Coordinate[]{
                new Coordinate(126.9578, 35.1334),
                new Coordinate(126.9655, 35.1213)
        });
    }

    @Test
    void LineString을_경도_위도_순서의_좌표_배열로_변환한다() {
        PathwayDTO dto = PathwayDTO.builder()
                .pathwayId(1L)
                .deptBaseId(21L)
                .destBaseId(11L)
                .difficulty(Difficulty.NORMAL)
                .coordinates(lineString())
                .build();

        PathwayCoordinatesArrayDTO result = PathwayCoordinatesArrayDTO.from(dto);

        // 각 점이 [getX(경도), getY(위도)] 순서로, 라인의 점 순서를 유지한 채 변환된다.
        assertThat(result.coordinates()).containsExactly(
                List.of(BigDecimal.valueOf(126.9578), BigDecimal.valueOf(35.1334)),
                List.of(BigDecimal.valueOf(126.9655), BigDecimal.valueOf(35.1213))
        );
    }

    @Test
    void 좌표_외_필드는_그대로_옮긴다() {
        PathwayDTO dto = PathwayDTO.builder()
                .pathwayId(7L)
                .deptBaseId(21L)
                .destBaseId(11L)
                .difficulty(Difficulty.HARD)
                .coordinates(lineString())
                .build();

        PathwayCoordinatesArrayDTO result = PathwayCoordinatesArrayDTO.from(dto);

        assertThat(result.pathwayId()).isEqualTo(7L);
        assertThat(result.deptBaseId()).isEqualTo(21L);
        assertThat(result.destBaseId()).isEqualTo(11L);
        assertThat(result.difficulty()).isEqualTo(Difficulty.HARD);
    }
}
