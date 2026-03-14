package com.econo_4factorial.newproject.travel.util;

import com.econo_4factorial.newproject.travel.TravelEvent;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.travel.dto.TravelAnalysisResult;
import com.econo_4factorial.newproject.travel.dto.res.CurrentPositionEventRes;
import com.econo_4factorial.newproject.travel.dto.res.TravelEventResponse;
import com.econo_4factorial.newproject.travel.dto.res.TravelEventResponseData;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TravelResponseMapper {
    public static TravelEventResponse toStartEventRes(TravelAnalysisResult result) {
        TravelEventResponseData data = toTravelEventResponseData(result);
        return new CurrentPositionEventRes(TravelEvent.START, data);
    }

    public static TravelEventResponse toPauseEventRes(TravelAnalysisResult result) {
        TravelEventResponseData data = toTravelEventResponseData(result);
        return new CurrentPositionEventRes(TravelEvent.PAUSE, data);
    }

    public static TravelEventResponse toKeepAliveEventRes() {
        return new CurrentPositionEventRes(TravelEvent.KEEP_ALIVE, null);
    }

    public static TravelEventResponse toRestartEventRes(TravelAnalysisResult result) {
        TravelEventResponseData data = toTravelEventResponseData(result);
        return new CurrentPositionEventRes(TravelEvent.RESTART, data);
    }

    public static TravelEventResponse toEndEventRes(TravelAnalysisResult result) {
        TravelEventResponseData data = toTravelEventResponseData(result);
        return new CurrentPositionEventRes(TravelEvent.END, data);
    }

    public static CurrentPositionEventRes toCurrentPositionEventRes(TravelAnalysisResult result)
    {
        TravelEventResponseData data = toTravelEventResponseData(result);
        return new CurrentPositionEventRes(TravelEvent.CURRENT_POSITION, data);
    }

    private static TravelEventResponseData toTravelEventResponseData(TravelAnalysisResult result) {
        RemainingTime remainingTime = result.travelRemainingTime();

        return TravelEventResponseData.builder()
                .index(result.closestIndex())
                .isArrived(result.isArrived())
                .isDeviation(result.isDeviation())
                .travelDistance(result.totalTravelDistance())
                .remainTimeToStopover(remainingTime.getToStopover().toMillis())
                .remainTimeToEnd(remainingTime.getToDestination().toMillis())
                .build();
    }
}
