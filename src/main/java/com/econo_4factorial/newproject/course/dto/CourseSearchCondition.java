package com.econo_4factorial.newproject.course.dto;

import lombok.Builder;

@Builder
public record CourseSearchCondition(
        Long mountainId,
        String sortBy
) {
    public static CourseSearchCondition of(Long mountainId, String sortBy) {
        return CourseSearchCondition.builder()
                .mountainId(mountainId)
                .sortBy(sortBy)
                .build();
    }
}
