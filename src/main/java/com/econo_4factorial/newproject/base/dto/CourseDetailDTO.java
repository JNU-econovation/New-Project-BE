package com.econo_4factorial.newproject.base.dto;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.domain.Difficulty;

public record CourseDetailDTO(
        Long courseId,
        String  courseName,
        Double courseLength,
        Long courseDuration,
        Difficulty difficulty
) {
    public static CourseDetailDTO from(Course course) {
        return new CourseDetailDTO(
                course.getId(),
                course.getName(),
                course.getLength(),
                course.getDuration(),
                course.getDifficulty()
        );
    }
}
