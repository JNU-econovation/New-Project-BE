package com.econo_4factorial.newproject.course.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Difficulty {
    EASY("쉬움"), NORMAL("보통"), HARD("어려움");

    private final String value;
}
