package com.econo_4factorial.newproject.user.dto.req;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CheckNicknameReq(
        @NotNull(message = ValidationMessage.NICKNAME_IS_REQUIRED)
        @Size(min = 2, max = 12, message = ValidationMessage.NICKNAME_LENGTH_INVALID)
        @Pattern(
                regexp = "^[가-힣a-zA-Z0-9]+$",
                message = ValidationMessage.NICKNAME_PATTERN_INVALID
        )
        String nickname
) {
}
