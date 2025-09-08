package com.econo_4factorial.newproject.user.dto.res;

public record GetRandomNicknameRes(
        String nickname
) {
    public static GetRandomNicknameRes from(String nickname) {
        return new GetRandomNicknameRes(nickname);
    }
}
