package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.TravelEvent;

public class StartEventRes extends TravelEventResponse{

    public StartEventRes(TravelEvent event, TravelEventResponseData data) {
        super(event, data);
    }
}
