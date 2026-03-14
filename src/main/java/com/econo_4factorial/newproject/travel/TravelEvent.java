package com.econo_4factorial.newproject.travel;

import com.econo_4factorial.newproject.travel.exception.NotExistEventException;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
public enum TravelEvent {
    AUTH("auth-user"),
    START("start"),
    CURRENT_POSITION("current-position"),
    PAUSE("pause"),
    KEEP_ALIVE("keep-alive"),
    RESTART("restart"),
    END("end");

    private static final Map<String, TravelEvent> eventMap = Map.of(
            "auth-user", TravelEvent.AUTH,
            "start", TravelEvent.START,
            "current-position", TravelEvent.CURRENT_POSITION,
            "pause", TravelEvent.PAUSE,
            "keep-alive", TravelEvent.KEEP_ALIVE,
            "restart", TravelEvent.RESTART,
            "end", TravelEvent.END
    );
    private final String name;

    TravelEvent(String name) {
        this.name = name;
    }

    public static TravelEvent fromName(String name) {
        return Optional.ofNullable(eventMap.get(name))
                .orElseThrow(NotExistEventException::new);
    }
}
