package com.econo_4factorial.newproject.travel.domain;

import com.econo_4factorial.newproject.travel.Status;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Builder
@Getter
public class TravelTrackingInfo {
    private final Long userId;
    private LocalDateTime startedAt;
    private Long courseId;
    private RemainingTime remainingTime;
    private final List<Point> paths;

    @Builder.Default
    private double totalTravelDistanceKm = 0;

    @Builder.Default
    private LocalDateTime endAt = null;

    @Builder.Default
    private Status status = Status.STARTED;

    @Builder.Default
    private Duration totalTravelTime = null;

    public Point getLastPoint() {
        return paths.getLast();
    }

    public void start(RemainingTime remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void currentPosition(Point userPoint, Double totalTravelDistance, RemainingTime remainingTime) {
        paths.add(userPoint);
        this.totalTravelDistanceKm = totalTravelDistance;
        this.remainingTime = remainingTime;
        this.status = Status.TRAVEL;
    }

    public void pause(Point userPoint, Double totalTravelDistance, RemainingTime remainingTime) {
        paths.add(userPoint);
        this.totalTravelDistanceKm = totalTravelDistance;
        this.remainingTime = remainingTime;
        this.status = Status.PAUSED;
    }

    public void reStart(Point userPoint, Double totalTravelDistance, RemainingTime remainingTime) {
        paths.add(userPoint);
        this.totalTravelDistanceKm = totalTravelDistance;
        this.remainingTime = remainingTime;
        this.status = Status.RESTARTED;
    }

    public void end(LocalDateTime endAt, Point userPoint, Double totalTravelDistance, RemainingTime remainingTime,
                    Duration totalTravelTime) {
        paths.add(userPoint);
        this.totalTravelDistanceKm = totalTravelDistance;
        this.remainingTime = remainingTime;
        this.endAt = endAt;
        this.status = Status.END;
        this.totalTravelTime = totalTravelTime;
    }
}
