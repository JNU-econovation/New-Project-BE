package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.dto.TravelRecordDTO;
import java.util.List;

public record GetTravelRecordByMonthRes(
        List<TravelRecordDTO> records
) {
    public static GetTravelRecordByMonthRes from(List<TravelRecordDTO> recordList) {
        return new GetTravelRecordByMonthRes(recordList);
    }
}
