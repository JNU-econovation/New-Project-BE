package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.dto.CourseSearchCondition;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.exception.BadRequestException.CourseNotFoundException;
import com.econo_4factorial.newproject.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course findByIdOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
    }

    public List<CourseWithBookmarkDTO> getAllCoursesWithBookmark(Long userId, Long mountainId, String sortBy) {
        CourseSearchCondition courseSearchCondition = CourseSearchCondition.of(mountainId, sortBy);
        return courseRepository.findAllByMountainIdWithBookmark(courseSearchCondition, mountainId, userId);
    }

}
