package com.econo_4factorial.newproject.course.dto.res;

import com.econo_4factorial.newproject.course.domain.Difficulty;

public record CourseDTO(
        Long id,
        String name,
        Double length,
        Long duration,
        Difficulty difficulty,
        Boolean bookmark
) {
}
