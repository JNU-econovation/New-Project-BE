package com.econo_4factorial.newproject.course.dto;

import com.econo_4factorial.newproject.course.domain.Difficulty;

public record CourseWithBookmarkDTO(
        Long id,
        String name,
        Double length,
        Long duration,
        Difficulty difficulty,
        Boolean bookmark
) {
}
