package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.travel.TravelEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelEventResponse {
       private TravelEvent event;
       private TravelEventResponseData data;
}
