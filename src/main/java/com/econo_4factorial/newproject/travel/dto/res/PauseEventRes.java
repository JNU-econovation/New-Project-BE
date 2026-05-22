package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.TravelEvent;

public class PauseEventRes extends TravelEventResponse {
    public PauseEventRes(TravelEvent event, TravelEventResponseData data) {
        super(event, data);
    }
}
