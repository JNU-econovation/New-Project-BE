package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.course.dto.CourseSearchCondition;
import com.econo_4factorial.newproject.course.dto.res.CourseDTO;
import com.econo_4factorial.newproject.course.repository.CourseCustomRepository;
import com.econo_4factorial.newproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseCustomRepository courseCustomRepository;

    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCoursesWithBookmark(Long userId, Long mountainId, String sortBy) {
        CourseSearchCondition courseSearchCondition = CourseSearchCondition.of(mountainId, sortBy);
        return courseCustomRepository.findAllByMountainIdWithBookmark(courseSearchCondition, mountainId, userId);
    }
}
