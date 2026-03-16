package com.econo_4factorial.newproject.course.exception.InternalServerException;

import com.econo_4factorial.newproject.common.exception.InternalServerException;
import com.econo_4factorial.newproject.course.exception.CourseErrorType;

public class ClosestCoordinateNotFoundException extends InternalServerException {
    public ClosestCoordinateNotFoundException() {
        super(CourseErrorType.CLOSEST_COORDINATE_NOT_FOUND_EXCEPTION);
    }
}
