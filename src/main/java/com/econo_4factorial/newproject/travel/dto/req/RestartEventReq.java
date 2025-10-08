package com.econo_4factorial.newproject.travel.dto.req;

import java.sql.Timestamp;

public record RestartEventReq(
        double[] coordinate,
        Long courseId,
        Timestamp time
) {
}
