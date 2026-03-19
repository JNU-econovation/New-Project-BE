package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.dto.TravelRecordDetailDTO;

import java.math.BigDecimal;
import java.util.List;

public record GetTravelRecordDetailRes (
        Long recordId,
        String displayName,
        Long startedAt,
        Long endAt,
        Long duration,
        Double length,
        List<List<BigDecimal>> coordinates,
        Long courseId,
        Integer mountainId
) {
    // Current service scope supports a single target mountain only.
    public static final Integer DEFAULT_MOUNTAIN_ID = 1;

    public static GetTravelRecordDetailRes from (TravelRecordDetailDTO recordDetail) {
        return new GetTravelRecordDetailRes(
                recordDetail.recordId(),
                recordDetail.displayName(),
                recordDetail.startedAt(),
                recordDetail.endAt(),
                recordDetail.duration(),
                recordDetail.length(),
                recordDetail.coordinates(),
                recordDetail.courseId(),
                DEFAULT_MOUNTAIN_ID
        );
    }
}
