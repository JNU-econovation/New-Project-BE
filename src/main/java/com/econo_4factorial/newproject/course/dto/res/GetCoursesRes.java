package com.econo_4factorial.newproject.course.dto.res;

import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import java.util.List;

public record GetCoursesRes(
        List<CourseWithBookmarkDTO> courses
) {
    public static GetCoursesRes from(List<CourseWithBookmarkDTO> courses) {
        return new GetCoursesRes(courses);
    }
}
