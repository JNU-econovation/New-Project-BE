package com.econo_4factorial.newproject.travel.dto.req;

import java.sql.Timestamp;

public record PauseEventReq(
        double[] coordinate,
        Long courseId,
        Timestamp time
) {
}
