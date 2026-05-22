package com.econo_4factorial.newproject.auth.dto.Req;

import jakarta.validation.constraints.NotBlank;

public record ReissueTokenReq(
        @NotBlank(message = "리프레쉬 토큰은 필수입니다.")
        String refreshToken
) {
}
