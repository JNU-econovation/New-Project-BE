package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.TravelEvent;

public class CurrentPositionEventRes extends TravelEventResponse{

    public CurrentPositionEventRes(TravelEvent event, TravelEventResponseData data) {
        super(event, data);
    }
}
