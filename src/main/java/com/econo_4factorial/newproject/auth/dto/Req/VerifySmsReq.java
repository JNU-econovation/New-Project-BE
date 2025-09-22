package com.econo_4factorial.newproject.auth.dto.Req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerifySmsReq(
        @NotBlank(message = ValidationMessage.PHONE_NUMBER_IS_REQUIRED)
        @Pattern(
                regexp = "^010-\\d{4}-\\d{4}$",
                message = ValidationMessage.PHONE_NUMBER_INVALID
        )
        String phoneNumber,

        @NotBlank(message = ValidationMessage.VERIFICATION_CODE_IS_REQUIRED)
        @Size(min = 6, max = 6, message = ValidationMessage.VERIFICATION_CODE_INVALID)
        String verificationCode
) {
}
