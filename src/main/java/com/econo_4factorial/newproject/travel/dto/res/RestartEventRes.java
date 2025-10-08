package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.TravelEvent;

public class RestartEventRes extends TravelEventResponse{
    public RestartEventRes(TravelEvent event, TravelEventResponseData data) {
        super(event, data);
    }
}
