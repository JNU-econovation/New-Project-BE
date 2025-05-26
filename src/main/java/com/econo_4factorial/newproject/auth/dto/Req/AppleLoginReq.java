package com.econo_4factorial.newproject.auth.dto.Req;

import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.common.annotation.ValidEmailPattern;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AppleLoginReq(
        @NotBlank(message = ValidationMessage.IDENTITY_TOKEN_REQUIRED)
        String identityToken,

        @NotBlank(message = ValidationMessage.EMAIL_INVALID_PATTERN)
        @ValidEmailPattern
        String email,

        @Valid
        FullName fullName
) {
        public AppleUserInfoDTO toAppleUserInfoDTO(String appleSub) {
                String name = this.fullName.familyName()+this.fullName.givenName();
                return new AppleUserInfoDTO(appleSub, name, this.email);
        }
}
