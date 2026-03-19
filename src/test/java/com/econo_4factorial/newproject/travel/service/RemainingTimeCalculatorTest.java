package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemainingTimeCalculatorTest {

    @Mock
    private TravelDistanceCalculator travelDistanceCalculator;

    @Mock
    private com.econo_4factorial.newproject.course.service.CourseService courseService;

    @InjectMocks
    private RemainingTimeCalculator remainingTimeCalculator;

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 남은거리를_기반으로_경유지와_도착지까지의_남은시간을_계산한다() {
        Long courseId = 1L;
        Point userPoint = 포인트를_생성한다(126.0, 37.0);
        Point peakPoint = 포인트를_생성한다(126.2, 37.2);
        Point destinationPoint = 포인트를_생성한다(126.3, 37.3);
        LineString courseLine = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(126.0, 37.0),
                new Coordinate(126.2, 37.2),
                new Coordinate(126.3, 37.3)
        });
        Course course = mock(Course.class);
        Base peakBase = mock(Base.class);
        Base destinationBase = mock(Base.class);
        given(courseService.findByIdOrThrow(courseId)).willReturn(course);
        given(course.getCoordinates()).willReturn(courseLine);
        given(course.getPeakBase()).willReturn(peakBase);
        given(course.getDestinationBase()).willReturn(destinationBase);
        given(peakBase.getGeoPoint()).willReturn(peakPoint);
        given(destinationBase.getGeoPoint()).willReturn(destinationPoint);
        given(travelDistanceCalculator.calculateRemainingDistanceToPointInCourse(courseLine, peakPoint, userPoint))
                .willReturn(120.0);
        given(travelDistanceCalculator.calculateRemainingDistanceToPointInCourse(courseLine, destinationPoint, userPoint))
                .willReturn(300.0);

        RemainingTime result = remainingTimeCalculator.calculateRemainingTime(courseId, userPoint);

        assertThat(result.getToStopover()).isEqualTo(Duration.ofSeconds(200));
        assertThat(result.getToDestination()).isEqualTo(Duration.ofSeconds(500));
        verify(courseService, times(2)).findByIdOrThrow(courseId);
    }

    @Test
    void 남은거리가_0이면_남은시간도_0초다() {
        Long courseId = 2L;
        Point userPoint = 포인트를_생성한다(127.0, 38.0);
        Point peakPoint = 포인트를_생성한다(127.1, 38.1);
        Point destinationPoint = 포인트를_생성한다(127.2, 38.2);
        LineString courseLine = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(127.0, 38.0),
                new Coordinate(127.1, 38.1),
                new Coordinate(127.2, 38.2)
        });
        Course course = mock(Course.class);
        Base peakBase = mock(Base.class);
        Base destinationBase = mock(Base.class);
        given(courseService.findByIdOrThrow(courseId)).willReturn(course);
        given(course.getCoordinates()).willReturn(courseLine);
        given(course.getPeakBase()).willReturn(peakBase);
        given(course.getDestinationBase()).willReturn(destinationBase);
        given(peakBase.getGeoPoint()).willReturn(peakPoint);
        given(destinationBase.getGeoPoint()).willReturn(destinationPoint);
        given(travelDistanceCalculator.calculateRemainingDistanceToPointInCourse(courseLine, peakPoint, userPoint))
                .willReturn(0.0);
        given(travelDistanceCalculator.calculateRemainingDistanceToPointInCourse(courseLine, destinationPoint, userPoint))
                .willReturn(0.0);

        RemainingTime result = remainingTimeCalculator.calculateRemainingTime(courseId, userPoint);

        assertThat(result.getToStopover()).isZero();
        assertThat(result.getToDestination()).isZero();
    }

    @Test
    void 코스를_찾지못하면_예외가_발생한다() {
        RuntimeException exception = new RuntimeException("course not found");
        given(courseService.findByIdOrThrow(3L)).willThrow(exception);

        assertThatThrownBy(() -> remainingTimeCalculator.calculateRemainingTime(3L, 포인트를_생성한다(128.0, 39.0)))
                .isSameAs(exception);
    }

    private Point 포인트를_생성한다(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }
}
