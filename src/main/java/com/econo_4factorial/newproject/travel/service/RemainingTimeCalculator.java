package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.service.CourseService;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemainingTimeCalculator {
    private final Double AVERAGE_HIKING_SPEED = 0.60; // 0.60m/s = 2.16km/h

    private final TravelDistanceCalculator travelDistanceCalculator;
    private final CourseService courseService;

    @Transactional(readOnly = true)
    public RemainingTime calculateRemainingTime(Long courseId, Point userPoint) {
        //Double remainingDistanceToStopOver = courseService.calculateRemainingDistanceToPeak(courseId, userPoint);
        //Double remainingDistanceToDestination = courseService.calculateRemainingDistanceToDestination(courseId, userPoint);
        Double remainingDistanceToStopOver = calculateRemainingDistanceToPeak(courseId, userPoint);
        Double remainingDistanceToDestination = calculateRemainingDistanceToDestination(courseId, userPoint);

        Duration remainingTimeToStopOver = Duration.ofMillis(
                (long) (remainingDistanceToStopOver / AVERAGE_HIKING_SPEED) * 1000);
        Duration remainingTimeToDestination = Duration.ofMillis(
                (long) (remainingDistanceToDestination / AVERAGE_HIKING_SPEED) * 1000);
        return new RemainingTime(remainingTimeToStopOver, remainingTimeToDestination);
    }

    private Double calculateRemainingDistanceToPeak(Long courseId, Point userPoint) {
        Course course = courseService.findByIdOrThrow(courseId);
        LineString lineStringOfCourse = course.getCoordinates();
        Point peakPoint = course.getPeakBase().getGeoPoint();

        return travelDistanceCalculator.calculateRemainingDistanceToPointInCourse(lineStringOfCourse, peakPoint,
                userPoint);
    }

    private Double calculateRemainingDistanceToDestination(Long courseId, Point userPoint) {
        Course course = courseService.findByIdOrThrow(courseId);
        LineString lineStringOfCourse = course.getCoordinates();
        Point destinationPoint = course.getDestinationBase().getGeoPoint();

        return travelDistanceCalculator.calculateRemainingDistanceToPointInCourse(lineStringOfCourse, destinationPoint,
                userPoint);
    }

}
