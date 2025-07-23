package com.econo_4factorial.newproject.auth.dto.Req;

import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record AppleLoginReq(
        @NotBlank(message = ValidationMessage.IDENTITY_TOKEN_REQUIRED)
        String identityToken,

        String email,

        FullName fullName
) {
        public AppleUserInfoDTO toAppleUserInfoDTO(String appleSub) {
                String name = this.fullName.getName();
                return new AppleUserInfoDTO(appleSub, name, this.email);
        }
}
