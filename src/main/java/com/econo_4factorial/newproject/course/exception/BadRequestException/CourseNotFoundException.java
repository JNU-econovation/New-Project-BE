package com.econo_4factorial.newproject.course.exception.BadRequestException;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.course.exception.CourseErrorType;

public class CourseNotFoundException extends BadRequestException {
    public CourseNotFoundException() {
        super(CourseErrorType.COURSE_NOT_FOUND_EXCEPTION);
    }
}
