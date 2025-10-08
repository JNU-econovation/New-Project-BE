package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.*;

public record AddPersonalInformationReq(
        @NotBlank(message = ValidationMessage.NAME_IS_REQUIRED)
        @Size(min = 2, max = 10, message = ValidationMessage.NAME_LENGTH_INVALID)
        @Pattern(regexp = "^[가-힣]+$", message = ValidationMessage.NAME_ONLY_KOREAN)
        String name,

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
        String bloodType
) {
}
