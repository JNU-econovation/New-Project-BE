package com.econo_4factorial.newproject.course.dto.req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddBookmarkReq(
        @NotNull(message = ValidationMessage.COURSE_ID_REQUIRED)
        @Positive(message = ValidationMessage.POSITIVE_NUMBER_REQUIRED)
        Long courseId
) {
}
