package com.econo_4factorial.newproject.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class UserAlert {

    @Column(name = "event_alert", nullable = false)
    private boolean eventAlert = true;

    @Column(name = "travel_deviation_alert", nullable = false)
    private boolean travelDeviationAlert = true;

    @Column(name = "accident_prone_area_alert", nullable = false)
    private boolean accidentProneAreaAlert = true;

    public void updateAlerts(boolean eventAlert, boolean travelDeviationAlert, boolean accidentProneAreaAlert) {
        this.eventAlert = eventAlert;
        this.travelDeviationAlert = travelDeviationAlert;
        this.accidentProneAreaAlert = accidentProneAreaAlert;
    }

}
