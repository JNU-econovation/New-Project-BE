package com.econo_4factorial.newproject.travel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.econo_4factorial.newproject.travel.Status;
import com.econo_4factorial.newproject.travel.domain.TravelTrackingInfo;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.travel.exception.TravelTrackingInfoNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

class TravelTrackingInfoStoreTest {

    private TravelTrackingInfoStore travelTrackingInfoStore;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        travelTrackingInfoStore = new TravelTrackingInfoStore();
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 산행정보가_없으면_상태는_UNSTARTED다() {
        Status result = travelTrackingInfoStore.getStatus(1L);

        assertThat(result).isEqualTo(Status.UNSTARTED);
        assertThat(travelTrackingInfoStore.isExistTravelTrackingInfo(1L)).isFalse();
    }

    @Test
    void 산행정보를_생성하고_시작상태를_조회한다() {
        LocalDateTime startedAt = LocalDateTime.of(2024, 5, 1, 9, 0);
        Point startPoint = 포인트를_생성한다(126.0, 37.0);

        travelTrackingInfoStore.makeInfo(1L, 10L, startedAt, startPoint);

        TravelTrackingInfo result = travelTrackingInfoStore.getInfo(1L);

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getCourseId()).isEqualTo(10L);
        assertThat(result.getStartedAt()).isEqualTo(startedAt);
        assertThat(result.getLastPoint()).isEqualTo(startPoint);
        assertThat(travelTrackingInfoStore.getStatus(1L)).isEqualTo(Status.STARTED);
        assertThat(travelTrackingInfoStore.isExistTravelTrackingInfo(1L)).isTrue();
    }

    @Test
    void 산행상태_업데이트를_순차적으로_반영한다() {
        Point startPoint = 포인트를_생성한다(126.0, 37.0);
        Point currentPoint = 포인트를_생성한다(126.1, 37.1);
        Point pausedPoint = 포인트를_생성한다(126.2, 37.2);
        Point restartedPoint = 포인트를_생성한다(126.3, 37.3);
        Point endPoint = 포인트를_생성한다(126.4, 37.4);
        RemainingTime startedRemainingTime = 남은시간을_생성한다(30, 60);
        RemainingTime updatedRemainingTime = 남은시간을_생성한다(10, 20);
        LocalDateTime endAt = LocalDateTime.of(2024, 5, 1, 12, 0);
        Duration totalTravelTime = Duration.ofHours(3);
        travelTrackingInfoStore.makeInfo(1L, 10L, LocalDateTime.of(2024, 5, 1, 9, 0), startPoint);

        travelTrackingInfoStore.start(1L, startedRemainingTime);
        travelTrackingInfoStore.currentPosition(1L, currentPoint, updatedRemainingTime, 1.5);
        travelTrackingInfoStore.pause(1L, pausedPoint, updatedRemainingTime, 2.5);
        travelTrackingInfoStore.reStart(1L, restartedPoint, updatedRemainingTime, 3.5);
        TravelTrackingInfo result = travelTrackingInfoStore.end(endAt, 1L, endPoint, updatedRemainingTime, 4.5,
                totalTravelTime);

        assertThat(result.getStatus()).isEqualTo(Status.END);
        assertThat(result.getRemainingTime()).isEqualTo(updatedRemainingTime);
        assertThat(result.getLastPoint()).isEqualTo(endPoint);
        assertThat(result.getTotalTravelDistanceKm()).isEqualTo(4.5);
        assertThat(result.getEndAt()).isEqualTo(endAt);
        assertThat(result.getTotalTravelTime()).isEqualTo(totalTravelTime);
        assertThat(result.getPaths()).hasSize(5);
    }

    @Test
    void 종료된_산행정보를_삭제하면_END상태가_유지되지_않고_UNSTARTED가_된다() {
        Point startPoint = 포인트를_생성한다(126.0, 37.0);
        Point endPoint = 포인트를_생성한다(126.4, 37.4);
        RemainingTime remainingTime = 남은시간을_생성한다(10, 20);
        LocalDateTime startedAt = LocalDateTime.of(2024, 5, 1, 9, 0);
        LocalDateTime endAt = LocalDateTime.of(2024, 5, 1, 12, 0);

        travelTrackingInfoStore.makeInfo(1L, 10L, startedAt, startPoint);
        travelTrackingInfoStore.start(1L, remainingTime);
        travelTrackingInfoStore.end(endAt, 1L, endPoint, remainingTime, 4.5, Duration.ofHours(3));

        assertThat(travelTrackingInfoStore.getStatus(1L)).isEqualTo(Status.END);

        travelTrackingInfoStore.deleteInfo(1L);

        assertThat(travelTrackingInfoStore.getStatus(1L)).isEqualTo(Status.UNSTARTED);
        assertThat(travelTrackingInfoStore.isExistTravelTrackingInfo(1L)).isFalse();
    }

    @Test
    void 산행정보가_없으면_마지막위치와_누적거리를_조회할수없다() {
        assertThatThrownBy(() -> travelTrackingInfoStore.getLastPoint(1L))
                .isInstanceOf(TravelTrackingInfoNotFoundException.class);

        assertThatThrownBy(() -> travelTrackingInfoStore.getTotalTravelDistance(1L))
                .isInstanceOf(TravelTrackingInfoNotFoundException.class);
    }

    @Test
    void 산행정보를_삭제한다() {
        travelTrackingInfoStore.makeInfo(1L, 10L, LocalDateTime.of(2024, 5, 1, 9, 0), 포인트를_생성한다(126.0, 37.0));

        travelTrackingInfoStore.deleteInfo(1L);

        assertThat(travelTrackingInfoStore.isExistTravelTrackingInfo(1L)).isFalse();
        assertThat(travelTrackingInfoStore.getStatus(1L)).isEqualTo(Status.UNSTARTED);
        assertThat(travelTrackingInfoStore.getInfo(1L)).isNull();
    }

    private Point 포인트를_생성한다(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    private RemainingTime 남은시간을_생성한다(long stopoverSeconds, long destinationSeconds) {
        return new RemainingTime(Duration.ofSeconds(stopoverSeconds), Duration.ofSeconds(destinationSeconds));
    }
}
