package com.econo_4factorial.newproject.base.dto;


import com.econo_4factorial.newproject.base.domain.Base;
import java.math.BigDecimal;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;

public record BaseDTO(
        Long baseId,
        String name,
        List<BigDecimal> coordinate

) {
    public static BaseDTO from(Base base) {
        Coordinate coordinate = base.getGeoPoint().getCoordinate();
        return new BaseDTO(
                base.getId(),
                base.getName(),
                List.of(
                        BigDecimal.valueOf(coordinate.getX()),
                        BigDecimal.valueOf(coordinate.getY())
                )
        );
    }
}
