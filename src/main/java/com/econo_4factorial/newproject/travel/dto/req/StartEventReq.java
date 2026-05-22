package com.econo_4factorial.newproject.travel.dto.req;

public record StartEventReq(
        double[] coordinate,
        Long courseId,
        Long time
) {

}
