package com.econo_4factorial.newproject.course.dto;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.common.constant.Difficulty;

public record CourseWithBookmarkDTO(
        Long id,
        String name,
        String displayName,
        Long peakBaseId,
        Double length,
        Long duration,
        Difficulty difficulty,
        Boolean bookmark,
        String image
) {
    public static CourseWithBookmarkDTO from(Course course, Boolean isBookmark){
        return new CourseWithBookmarkDTO(
                course.getId(),
                course.getName(),
                course.getDisplayName(),
                course.getPeakBaseId(),
                course.getLength(),
                course.getDuration(),
                course.getDifficulty(),
                isBookmark,
                course.getImageUrl()
        );
    }
}
