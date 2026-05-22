package com.econo_4factorial.newproject.travel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

class TravelDistanceCalculatorTest {

    private TravelDistanceCalculator travelDistanceCalculator;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        travelDistanceCalculator = new TravelDistanceCalculator();
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 이전_위치가_없으면_이동거리는_0이다() {
        Point currentPoint = 포인트를_생성한다(127.0, 37.0);

        Double distance = travelDistanceCalculator.calculateDistanceFromLastLocation(null, currentPoint);

        assertThat(distance).isZero();
    }

    @Test
    void 두_좌표_사이의_거리를_킬로미터로_계산한다() {
        Point previousPoint = 포인트를_생성한다(0.0, 0.0);
        Point currentPoint = 포인트를_생성한다(0.0, 1.0);

        Double distance = travelDistanceCalculator.calculateDistanceFromLastLocation(previousPoint, currentPoint);

        assertThat(distance).isCloseTo(111.19, offset(0.5));
    }

    @Test
    void 두_포인트_사이의_거리를_미터로_계산한다() {
        Point closestPoint = 포인트를_생성한다(0.0, 0.0);
        Point userPoint = 포인트를_생성한다(0.0, 1.0);

        double distance = travelDistanceCalculator.calculateDistanceBetweenPoints(closestPoint, userPoint);

        assertThat(distance).isCloseTo(111194.9, offset(500.0));
    }

    @Test
    void 코스에서_목표지점까지_남은_거리를_계산한다() {
        LineString courseLine = 라인스트링을_생성한다(
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 1.0),
                new Coordinate(0.0, 2.0)
        );
        Point targetPoint = 포인트를_생성한다(0.0, 2.0);
        Point userPoint = 포인트를_생성한다(0.0, 1.0);

        Double remainingDistance = travelDistanceCalculator.calculateRemainingDistanceToPointInCourse(
                courseLine,
                targetPoint,
                userPoint
        );

        assertThat(remainingDistance).isCloseTo(111194.9, offset(500.0));
    }

    @Test
    void 사용자_위치가_목표를_지났으면_남은_거리는_0이다() {
        LineString courseLine = 라인스트링을_생성한다(
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 1.0),
                new Coordinate(0.0, 2.0)
        );
        Point targetPoint = 포인트를_생성한다(0.0, 1.0);
        Point userPoint = 포인트를_생성한다(0.0, 1.5);

        Double remainingDistance = travelDistanceCalculator.calculateRemainingDistanceToPointInCourse(
                courseLine,
                targetPoint,
                userPoint
        );

        assertThat(remainingDistance).isZero();
    }

    private Point 포인트를_생성한다(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    private LineString 라인스트링을_생성한다(Coordinate... coordinates) {
        return geometryFactory.createLineString(coordinates);
    }
}
