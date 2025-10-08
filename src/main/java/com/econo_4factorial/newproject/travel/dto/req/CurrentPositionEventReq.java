package com.econo_4factorial.newproject.travel.dto.req;

public record CurrentPositionEventReq(
        double[] coordinate,
        Long courseId
) {
}
