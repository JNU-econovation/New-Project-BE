package com.econo_4factorial.newproject.auth.dto.Res;

public record VerifySmsRes(
        String phoneNumber
) {
    public static VerifySmsRes from(String phoneNumber) {
        return new VerifySmsRes(phoneNumber);
    }
}
