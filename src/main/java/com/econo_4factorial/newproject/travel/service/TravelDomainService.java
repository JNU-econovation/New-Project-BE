package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.course.dto.ClosestCoordinateInfo;
import com.econo_4factorial.newproject.course.service.CourseService;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.travel.dto.TravelAnalysisResult;
import com.econo_4factorial.newproject.travel.util.GeoUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelDomainService {
    private final Long DEVIATION_THRESHOLD = 50L;

    private final CourseService courseService;
    private final RemainingTimeCalculator remainingTimeCalculator;
    private final TravelDistanceCalculator travelDistanceCalculator;

    public TravelAnalysisResult analyzeTravelStatus(Long courseId, Point prevPoint, Point userPoint,
                                                    Double prevTotalTravelDistance) {
        ClosestCoordinateInfo closestCoordinate = courseService.findClosestCoordinateWithIndex(courseId, userPoint);
        Point closestPoint = GeoUtil.toPoint(closestCoordinate.coordinate());

        Boolean isArrived = courseService.isArrived(courseId, userPoint);
        Boolean isDeviation = isDeviation(closestPoint, userPoint);
        //Boolean isDeviation = courseService.isDeviation(closestCoordinate.coordinate(), userPoint);
        RemainingTime travelRemainingTime = remainingTimeCalculator.calculateRemainingTime(courseId, userPoint);
        Double travelDistance = travelDistanceCalculator.calculateDistanceFromLastLocation(prevPoint, userPoint);
        Double totalTravelDistance =
                Math.round((prevTotalTravelDistance + travelDistance) * 100) / 100.0; //소수점 둘 째 짜리까지 표기 km단위.

        return new TravelAnalysisResult(
                closestCoordinate.index(),
                isArrived,
                isDeviation,
                totalTravelDistance,
                travelRemainingTime
        );
    }

    private Boolean isDeviation(Point closestPoint, Point userPoint) {
        double distance = travelDistanceCalculator.calculateDistanceBetweenPoints(closestPoint, userPoint);
        return distance > DEVIATION_THRESHOLD;
    }
}
