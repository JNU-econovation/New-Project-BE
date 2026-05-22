package com.econo_4factorial.newproject.travel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.travel.Status;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

class TravelTrackingInfoTest {

    private GeometryFactory geometryFactory;
    private TravelTrackingInfo travelTrackingInfo;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        travelTrackingInfo = TravelTrackingInfo.builder()
                .userId(1L)
                .courseId(10L)
                .startedAt(LocalDateTime.of(2024, 1, 1, 9, 0))
                .paths(new ArrayList<>(java.util.List.of(포인트를_생성한다(126.0, 37.0))))
                .build();
    }

    @Test
    void 시작하면_남은시간만_갱신하고_상태는_STARTED를_유지한다() {
        RemainingTime remainingTime = 남은시간을_생성한다(30, 60);

        travelTrackingInfo.start(remainingTime);

        assertThat(travelTrackingInfo.getRemainingTime()).isEqualTo(remainingTime);
        assertThat(travelTrackingInfo.getStatus()).isEqualTo(Status.STARTED);
        assertThat(travelTrackingInfo.getTotalTravelDistanceKm()).isZero();
    }

    @Test
    void 현재위치_업데이트시_경로_거리_남은시간과_상태를_갱신한다() {
        Point currentPoint = 포인트를_생성한다(126.1, 37.1);
        RemainingTime remainingTime = 남은시간을_생성한다(20, 50);

        travelTrackingInfo.currentPosition(currentPoint, 3.5, remainingTime);

        assertThat(travelTrackingInfo.getStatus()).isEqualTo(Status.TRAVEL);
        assertThat(travelTrackingInfo.getTotalTravelDistanceKm()).isEqualTo(3.5);
        assertThat(travelTrackingInfo.getRemainingTime()).isEqualTo(remainingTime);
        assertThat(travelTrackingInfo.getPaths()).hasSize(2);
        assertThat(travelTrackingInfo.getLastPoint()).isEqualTo(currentPoint);
    }

    @Test
    void 일시정지시_상태를_PAUSED로_갱신한다() {
        Point pausedPoint = 포인트를_생성한다(126.2, 37.2);
        RemainingTime remainingTime = 남은시간을_생성한다(15, 40);

        travelTrackingInfo.pause(pausedPoint, 4.2, remainingTime);

        assertThat(travelTrackingInfo.getStatus()).isEqualTo(Status.PAUSED);
        assertThat(travelTrackingInfo.getTotalTravelDistanceKm()).isEqualTo(4.2);
        assertThat(travelTrackingInfo.getLastPoint()).isEqualTo(pausedPoint);
    }

    @Test
    void 재시작시_상태를_RESTARTED로_갱신한다() {
        Point restartedPoint = 포인트를_생성한다(126.3, 37.3);
        RemainingTime remainingTime = 남은시간을_생성한다(10, 30);

        travelTrackingInfo.reStart(restartedPoint, 5.7, remainingTime);

        assertThat(travelTrackingInfo.getStatus()).isEqualTo(Status.RESTARTED);
        assertThat(travelTrackingInfo.getTotalTravelDistanceKm()).isEqualTo(5.7);
        assertThat(travelTrackingInfo.getLastPoint()).isEqualTo(restartedPoint);
    }

    @Test
    void 종료시_상태와_종료시각_총시간을_갱신한다() {
        Point endPoint = 포인트를_생성한다(126.4, 37.4);
        RemainingTime remainingTime = 남은시간을_생성한다(0, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        Duration totalTravelTime = Duration.ofHours(3);

        travelTrackingInfo.end(endAt, endPoint, 8.1, remainingTime, totalTravelTime);

        assertThat(travelTrackingInfo.getStatus()).isEqualTo(Status.END);
        assertThat(travelTrackingInfo.getEndAt()).isEqualTo(endAt);
        assertThat(travelTrackingInfo.getTotalTravelTime()).isEqualTo(totalTravelTime);
        assertThat(travelTrackingInfo.getTotalTravelDistanceKm()).isEqualTo(8.1);
        assertThat(travelTrackingInfo.getLastPoint()).isEqualTo(endPoint);
    }

    private Point 포인트를_생성한다(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    private RemainingTime 남은시간을_생성한다(long stopoverMinutes, long destinationMinutes) {
        return new RemainingTime(Duration.ofMinutes(stopoverMinutes), Duration.ofMinutes(destinationMinutes));
    }
}
