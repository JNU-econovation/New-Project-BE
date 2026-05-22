package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.common.annotation.ValidEmailPattern;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProfileSettingReq(
        @NotBlank(message = ValidationMessage.NAME_IS_REQUIRED)
        @Size(min = 2, max = 10, message = ValidationMessage.NAME_LENGTH_INVALID)
        @Pattern(regexp = "^[가-힣]+$", message = ValidationMessage.NAME_ONLY_KOREAN)
        String name,

        @NotBlank(message = ValidationMessage.EMAIL_IS_REQUIRED)
        @ValidEmailPattern
        String email,

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

        @NotNull(message = ValidationMessage.WEIGHT_IS_REQUIRED)
        @Positive(message = ValidationMessage.WEIGHT_POSITIVE)
        @Max(value = 999, message = ValidationMessage.WEIGHT_MAX_LENGTH)
        Long weight,

        @NotNull(message = ValidationMessage.HEIGHT_IS_REQUIRED)
        @Positive(message = ValidationMessage.HEIGHT_POSITIVE)
        @Max(value = 999, message = ValidationMessage.HEIGHT_MAX_LENGTH)
        Long height,

        @NotBlank(message = ValidationMessage.BLOOD_TYPE_IS_REQUIRED)
        @Pattern(regexp = "^(A|B|O|AB)$", message = ValidationMessage.INVALID_BLOOD_TYPE)
        String bloodType,

        @Size(max = 200, message = ValidationMessage.ETC_LENGTH_INVALID)
        String etc
) {
}
