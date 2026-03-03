package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import com.econo_4factorial.newproject.user.domain.ImageFileFormat;
import jakarta.validation.constraints.NotNull;

public record IssuePresignedUrlReq(
        @NotNull(message = ValidationMessage.FILE_FORMAT_IS_REQUIRED)
        ImageFileFormat imageFileFormat
) {
}
