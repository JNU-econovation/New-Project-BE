package com.econo_4factorial.newproject.auth.dto.Req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SendSmsReq(
        @NotBlank(message = ValidationMessage.PHONE_NUMBER_IS_REQUIRED)
        @Pattern(
                regexp = "^010-\\d{4}-\\d{4}$",
                message = ValidationMessage.PHONE_NUMBER_INVALID
        )
        String phoneNumber
) {
}
