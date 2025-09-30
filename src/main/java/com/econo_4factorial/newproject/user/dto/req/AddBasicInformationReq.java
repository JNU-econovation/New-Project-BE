package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.common.annotation.ValidEmailPattern;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddBasicInformationReq(
        @NotBlank(message = ValidationMessage.NICKNAME_IS_REQUIRED)
        @Size(min = 2, max = 12, message = ValidationMessage.NICKNAME_LENGTH_INVALID)
        @Pattern(
                regexp = "^[가-힣a-zA-Z0-9]+$",
                message = ValidationMessage.NICKNAME_PATTERN_INVALID
        )
        String nickname,
        @NotBlank(message = ValidationMessage.PHONE_NUMBER_IS_REQUIRED)
        @Pattern(
                regexp = "^010-\\d{4}-\\d{4}$",
                message = ValidationMessage.PHONE_NUMBER_INVALID
        )
        String phoneNumber,
        @NotBlank(message = ValidationMessage.EMAIL_IS_REQUIRED)
        @ValidEmailPattern
        String email
) {
}
