package com.econo_4factorial.newproject.course.dto.res;

import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import java.util.List;

public record GetBookmarkListRes(
        List<CourseWithBookmarkDTO> bookmarkList
) {
    public static GetBookmarkListRes from(List<CourseWithBookmarkDTO> courses) {
        return new GetBookmarkListRes(courses);
    }
}
