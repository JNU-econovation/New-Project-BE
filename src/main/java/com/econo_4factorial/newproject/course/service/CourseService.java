package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.base.dto.CourseDetailDTO;
import com.econo_4factorial.newproject.course.dto.CourseSearchCondition;
import com.econo_4factorial.newproject.course.dto.res.CourseDTO;
import com.econo_4factorial.newproject.course.repository.CourseCustomRepository;
import com.econo_4factorial.newproject.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCoursesWithBookmark(Long userId, Long mountainId, String sortBy) {
        CourseSearchCondition courseSearchCondition = CourseSearchCondition.of(mountainId, sortBy);
        return courseRepository.findAllByMountainIdWithBookmark(courseSearchCondition, mountainId, userId);
    }

    @Transactional(readOnly = true)
    public CourseDetailDTO getCourseDetailsByCourseId(Long courseId) {
        return courseRepository.findById(courseId)
                .map(CourseDetailDTO::from)
                .orElseThrow(() -> new IllegalArgumentException("코스를 찾을 수 없습니다. courseId: " + courseId)); // 머지 후 COURSENOTFOUNDEXCEPTION으로 리팩토링
    }
}
