package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.base.dto.CourseDetailDTO;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.dto.ClosestCoordinateInfo;
import com.econo_4factorial.newproject.course.dto.CourseSearchCondition;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.exception.BadRequestException.CourseNotFoundException;
import com.econo_4factorial.newproject.course.repository.CourseRepository;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.service.PathwayService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
    private final Long TRUE = 1L;
    private final Long FALSE = 0L;

    private final CourseRepository courseRepository;
    private final PathwayService pathwayService;
    private final CourseLocationMatcher courseLocationMatcher;

    public Course findByIdOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<CourseWithBookmarkDTO> getAllCoursesWithBookmark(Long userId, Long mountainId, String sortBy) {
        CourseSearchCondition courseSearchCondition = CourseSearchCondition.of(mountainId, sortBy);
        return courseRepository.findAllByMountainIdWithBookmark(courseSearchCondition, mountainId, userId);
    }

    @Transactional(readOnly = true)
    public CourseDetailDTO getCourseDetailsByCourseId(Long courseId) {
        return courseRepository.findById(courseId)
                .map(CourseDetailDTO::from)
                .orElseThrow(CourseNotFoundException::new);
    }

    public ClosestCoordinateInfo findClosestCoordinateWithIndex(Long courseId, Point userPoint) {
        List<PathwayDTO> pathwayList = pathwayService.getPathwaysByCourseId(courseId);

        Coordinate[] coordinatesOfCourse = pathwayList.stream()
                .flatMap(pathway -> Arrays.stream(pathway.coordinates().getCoordinates()))
                .toArray(Coordinate[]::new);
        Coordinate userCoordinate = userPoint.getCoordinate();
        return courseLocationMatcher.findClosestCoordinateIndex(coordinatesOfCourse, userCoordinate);
    }

    public Boolean isArrived(Long courseId, Point userPoint) {
        long result = courseRepository.isUserArrivedDestination(courseId, userPoint);
        if (result == TRUE) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /*
    public Boolean isDeviation(Coordinate closestCoordinate, Point userPoint) {
        Point point = GeoUtil.toPoint(closestCoordinate);
        long result = courseRepository.isUserDeviated(point, userPoint);
        if (result == TRUE)
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

     */

    /*
    public Double calculateRemainingDistanceToPeak(Long courseId, Point userPoint) {
        return courseRepository.getRemainingDistanceToPeak(courseId, userPoint);
    }

    public Double calculateRemainingDistanceToDestination(Long courseId, Point userPoint) {
        return courseRepository.getRemainingDistanceToDestination(courseId, userPoint);
    }
     */
}
