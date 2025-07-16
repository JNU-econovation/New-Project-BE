package com.econo_4factorial.newproject.course.exception.BadRequestException;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.course.exception.CourseErrorType;

public class BookmarkNotFoundException extends BadRequestException {
    public BookmarkNotFoundException() {
        super(CourseErrorType.BOOKMARK_NOT_FOUND_EXCEPTION);
    }
}
