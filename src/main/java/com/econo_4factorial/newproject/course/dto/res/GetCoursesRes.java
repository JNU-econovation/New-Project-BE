package com.econo_4factorial.newproject.course.dto.res;

import java.util.List;

public record GetCoursesRes(
        List<CourseDTO> courses
) {
    public static GetCoursesRes from(List<CourseDTO> courses) {
        return new GetCoursesRes(courses);
    }
}
