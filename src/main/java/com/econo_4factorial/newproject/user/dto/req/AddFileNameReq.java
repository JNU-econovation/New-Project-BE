package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record AddFileNameReq(
        @NotBlank(message = ValidationMessage.FILE_NAME_IS_REQUIRED)
        String fileName
) {
}
