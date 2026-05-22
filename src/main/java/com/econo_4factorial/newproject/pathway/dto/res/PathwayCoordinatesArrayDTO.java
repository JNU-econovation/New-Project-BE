package com.econo_4factorial.newproject.pathway.dto.res;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.locationtech.jts.geom.LineString;

public record PathwayCoordinatesArrayDTO(
        Long pathwayId,
        Long deptBaseId,
        Long destBaseId,
        Difficulty difficulty,
        List<List<BigDecimal>> coordinates
) {
    public static PathwayCoordinatesArrayDTO from(PathwayDTO pathwayDTO) {
        List<List<BigDecimal>> coordinates = convertCoordinatesFromLineString(pathwayDTO.coordinates());
        return new PathwayCoordinatesArrayDTO(
                pathwayDTO.pathwayId(), pathwayDTO.deptBaseId(), pathwayDTO.destBaseId(), pathwayDTO.difficulty(),
                coordinates
        );
    }

    private static List<List<BigDecimal>> convertCoordinatesFromLineString(LineString coordinates) {
        return Stream.of(coordinates.getCoordinates())
                .map(coordinate -> List.of(
                        BigDecimal.valueOf(coordinate.getX()),
                        BigDecimal.valueOf(coordinate.getY())
                ))
                .toList();
    }
}
