package com.econo_4factorial.newproject.base.dto.res;

import com.econo_4factorial.newproject.base.dto.CourseDetailDTO;

public record GetCourseDetailsRes(
        CourseDetailDTO courseDetail
) {
    public static GetCourseDetailsRes from(CourseDetailDTO courseDetail) {
        return new GetCourseDetailsRes(courseDetail);
    }
}
