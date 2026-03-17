package com.econo_4factorial.newproject.travel.util;

import com.econo_4factorial.newproject.travel.TravelEvent;
import com.econo_4factorial.newproject.travel.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventPolicy {
    private final Map<Status, Set<TravelEvent>> policyMap = Map.of(
            Status.UNSTARTED, Set.of(TravelEvent.START),
            Status.STARTED, Set.of(TravelEvent.CURRENT_POSITION, TravelEvent.END, TravelEvent.PAUSE),
            Status.TRAVEL, Set.of(TravelEvent.PAUSE, TravelEvent.END, TravelEvent.CURRENT_POSITION),
            Status.PAUSED, Set.of(TravelEvent.RESTART, TravelEvent.END, TravelEvent.KEEP_ALIVE),
            Status.RESTARTED, Set.of(TravelEvent.CURRENT_POSITION, TravelEvent.END)
    );

    public boolean isAllowed(Status status, TravelEvent event) {
        return policyMap.get(status).contains(event);
    }
}
