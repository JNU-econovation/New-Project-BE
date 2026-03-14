package com.econo_4factorial.newproject.travel.dto;

import org.locationtech.jts.geom.Point;

public record TravelAnalysisInput(
        Long courseId,
        Point prevPoint,
        Point userPoint,
        Double totalTravelDistance
) {
}
