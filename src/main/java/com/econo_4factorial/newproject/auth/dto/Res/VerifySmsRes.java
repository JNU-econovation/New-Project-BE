package com.econo_4factorial.newproject.auth.dto.Res;

public record VerifySmsRes(
        String phoneNumber
) {
    public static VerifySmsRes from(String verificationCode) {
        return new VerifySmsRes(verificationCode);
    }
}
