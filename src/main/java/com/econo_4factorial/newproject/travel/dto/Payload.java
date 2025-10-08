package com.econo_4factorial.newproject.travel.dto;

import java.util.Map;

public record Payload(
        String event,
        Map<String, Object> data
) {

}
