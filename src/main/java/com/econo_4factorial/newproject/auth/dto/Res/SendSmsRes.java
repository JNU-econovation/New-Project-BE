package com.econo_4factorial.newproject.auth.dto.Res;

public record SendSmsRes(
        String phoneNumber
) {
    public static SendSmsRes from(String phoneNumber) {
        return new SendSmsRes(phoneNumber);
    }
}
