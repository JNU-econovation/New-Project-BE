package com.econo_4factorial.newproject.auth.dto.Req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record FullName(
        @NotBlank(message = ValidationMessage.FAMILY_NAME_REQUIRED)
        String familyName,

        @NotBlank(message = ValidationMessage.GIVEN_NAME_REQUIRED)
        String givenName
) {
}
