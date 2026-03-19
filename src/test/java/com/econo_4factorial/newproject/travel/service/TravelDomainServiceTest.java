package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.course.dto.ClosestCoordinateInfo;
import com.econo_4factorial.newproject.course.service.CourseService;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.travel.dto.TravelAnalysisResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TravelDomainServiceTest {

    @Mock
    private CourseService courseService;

    @Mock
    private RemainingTimeCalculator remainingTimeCalculator;

    @Mock
    private TravelDistanceCalculator travelDistanceCalculator;

    private TravelDomainService travelDomainService;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        travelDomainService = new TravelDomainService(courseService, remainingTimeCalculator, travelDistanceCalculator);
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 가장_가까운_좌표와_남은시간_이동거리_도착여부를_종합해_분석결과를_반환한다() {
        Point prevPoint = 포인트를_생성한다(126.0, 37.0);
        Point userPoint = 포인트를_생성한다(126.1, 37.1);
        RemainingTime remainingTime = new RemainingTime(Duration.ofMinutes(15), Duration.ofMinutes(30));

        given(courseService.findClosestCoordinateWithIndex(eq(1L), eq(userPoint)))
                .willReturn(new ClosestCoordinateInfo(new Coordinate(126.11, 37.11), 7));
        given(courseService.isArrived(1L, userPoint)).willReturn(true);
        given(remainingTimeCalculator.calculateRemainingTime(1L, userPoint)).willReturn(remainingTime);
        given(travelDistanceCalculator.calculateDistanceFromLastLocation(prevPoint, userPoint)).willReturn(0.456);
        given(travelDistanceCalculator.calculateDistanceBetweenPoints(any(Point.class), eq(userPoint))).willReturn(49.0);

        TravelAnalysisResult result = travelDomainService.analyzeTravelStatus(1L, prevPoint, userPoint, 1.234);

        assertThat(result.closestIndex()).isEqualTo(7);
        assertThat(result.isArrived()).isTrue();
        assertThat(result.isDeviation()).isFalse();
        assertThat(result.totalTravelDistance()).isEqualTo(1.69);
        assertThat(result.travelRemainingTime()).isEqualTo(remainingTime);
    }

    @Test
    void 가장_가까운_좌표와의_거리가_50미터를_넘으면_이탈로_판단한다() {
        Point prevPoint = 포인트를_생성한다(126.0, 37.0);
        Point userPoint = 포인트를_생성한다(126.1, 37.1);
        RemainingTime remainingTime = new RemainingTime(Duration.ofMinutes(10), Duration.ofMinutes(20));

        given(courseService.findClosestCoordinateWithIndex(eq(2L), eq(userPoint)))
                .willReturn(new ClosestCoordinateInfo(new Coordinate(126.11, 37.11), 3));
        given(courseService.isArrived(2L, userPoint)).willReturn(false);
        given(remainingTimeCalculator.calculateRemainingTime(2L, userPoint)).willReturn(remainingTime);
        given(travelDistanceCalculator.calculateDistanceFromLastLocation(prevPoint, userPoint)).willReturn(1.0);
        given(travelDistanceCalculator.calculateDistanceBetweenPoints(any(Point.class), eq(userPoint))).willReturn(51.0);

        TravelAnalysisResult result = travelDomainService.analyzeTravelStatus(2L, prevPoint, userPoint, 0.0);

        assertThat(result.isDeviation()).isTrue();
        assertThat(result.totalTravelDistance()).isEqualTo(1.0);
    }

    private Point 포인트를_생성한다(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }
}
