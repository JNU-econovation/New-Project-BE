package com.econo_4factorial.newproject.travel.domain.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RemainingTime {
    private Duration toStopover;
    private Duration toDestination;

    public RemainingTime(Duration toStopover, Duration toDestination) {
        this.toStopover = toStopover;
        this.toDestination = toDestination;
    }

    public void updateRemainTimeToStopover(Duration time) {
        this.toStopover = time;
    }

    public void updateRemainTimeToDestination(Duration time) {
        this.toDestination = time;
    }
}
