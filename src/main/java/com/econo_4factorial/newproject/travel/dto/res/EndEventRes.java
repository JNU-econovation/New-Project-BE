package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.TravelEvent;

public class EndEventRes extends TravelEventResponse{
    public EndEventRes(TravelEvent event, TravelEventResponseData data) {
        super(event, data);
    }
}
