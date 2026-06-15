package com.econo_4factorial.newproject.mountain.domain;

import java.text.Normalizer;

public record SearchKeyword(String value) {

    private static final String INITIAL_CONSTANTS = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";

    public SearchKeyword {
        value = normalize(value);
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean isInitials() {
        if (isEmpty()) {
            return false;
        }
        return value.chars().allMatch(c -> INITIAL_CONSTANTS.indexOf(c) >= 0);
    }

    private static String normalize(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        return Normalizer.normalize(raw.strip(), Normalizer.Form.NFC);
    }
}
