package com.econo_4factorial.newproject.course.dto;

import org.locationtech.jts.geom.Coordinate;

public record ClosestCoordinateInfo(
        Coordinate coordinate,
        Integer index
) {
}
