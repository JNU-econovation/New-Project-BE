package com.econo_4factorial.newproject.travel.dto.req;

public record EndEventReq(
        double[] coordinate,
        Long courseId,
        Long time,
        Long totalTravelTime
) {
}
