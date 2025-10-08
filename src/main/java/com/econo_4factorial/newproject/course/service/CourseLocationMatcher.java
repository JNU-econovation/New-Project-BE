package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.course.dto.ClosestCoordinateInfo;
import com.econo_4factorial.newproject.course.exception.InternalServerException.ClosestCoordinateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Component
public class CourseLocationMatcher {
    public ClosestCoordinateInfo findClosestCoordinateIndex(Coordinate[] coordinatesOfCourse, Coordinate userCoordinate) {

        Optional<Integer> closestCoordinateIndex = IntStream.range(0, coordinatesOfCourse.length)
                .boxed()
                .min(Comparator.comparingDouble(i-> coordinatesOfCourse[i].distance(userCoordinate)));

        if(closestCoordinateIndex.isPresent()) {
            Integer index = closestCoordinateIndex.get();
            Coordinate coordinate = coordinatesOfCourse[index];
            log.info("가장 가까운 인덱스 : {}", index);
            log.info("가장 가까운 좌표 : {}", coordinatesOfCourse[index]);
            return new ClosestCoordinateInfo(coordinate, index);
        }
        else
            throw new ClosestCoordinateNotFoundException();
    }

}
