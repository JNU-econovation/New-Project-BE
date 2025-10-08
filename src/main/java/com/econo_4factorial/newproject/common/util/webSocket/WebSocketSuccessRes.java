package com.econo_4factorial.newproject.common.util.webSocket;

import com.econo_4factorial.newproject.travel.TravelEvent;

public record WebSocketSuccessRes (
        String event,
        Object data,
        String Status
){
    public static WebSocketSuccessRes ok(TravelEvent event, Object data) {
        return new WebSocketSuccessRes(event.getName(), data, "success");
    }
}
