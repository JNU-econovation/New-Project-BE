package com.econo_4factorial.newproject.course.repository;

import com.econo_4factorial.newproject.course.dto.CourseSearchCondition;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;

import java.util.List;

public interface CourseCustomRepository {
    List<CourseWithBookmarkDTO> findAllByMountainIdWithBookmark(CourseSearchCondition courseSearchCondition, Long mountainId, Long userId);
}
