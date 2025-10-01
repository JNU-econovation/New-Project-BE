package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ProfileImageUrlReq(
        @NotBlank(message = ValidationMessage.IMAGE_URL_REQUIRED)
        @URL(message = ValidationMessage.IMAGE_URL_INVALID)
        String imageUrl
) {
}