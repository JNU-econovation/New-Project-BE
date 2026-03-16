package com.econo_4factorial.newproject.travel.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TravelEventResponseData {
    private Integer index;
    private Boolean isArrived;
    private Boolean isDeviation;
    private Double travelDistance;
    private Long remainTimeToStopover;
    private Long remainTimeToEnd;
}
