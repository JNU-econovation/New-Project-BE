package com.econo_4factorial.newproject.travel.dto;

import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;

public record TravelAnalysisResult (
        Integer closestIndex,
        Boolean isArrived,
        Boolean isDeviation,
        Double totalTravelDistance,
        RemainingTime travelRemainingTime
){
}
