package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.dto.TravelRecordDetailDTO;

import java.math.BigDecimal;
import java.util.List;

public record GetTravelRecordDetailRes (
        Long recordId,
        String displayName,
        Long staredAt,
        Long endAt,
        Long duration,
        Double length,
        List<List<BigDecimal>> coordinates,
        Long courseId,
        Integer mountainId
) {
    public static GetTravelRecordDetailRes from (TravelRecordDetailDTO recordDetail) {
        return new GetTravelRecordDetailRes(
                recordDetail.recordId(),
                recordDetail.displayName(),
                recordDetail.staredAt(),
                recordDetail.endAt(),
                recordDetail.duration(),
                recordDetail.length(),
                recordDetail.coordinates(),
                recordDetail.courseId(),
                1 //무등산 하드 코딩. 수정 필요
        );
    }
}
