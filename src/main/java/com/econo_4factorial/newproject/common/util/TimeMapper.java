package com.econo_4factorial.newproject.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeMapper {
    private static final ZoneId ASIA_SEOUL = ZoneId.of("Asia/Seoul");

    public static LocalDateTime toLocalTime(Long time) {
        return Instant.ofEpochMilli(time)
                .atZone(ASIA_SEOUL)
                .toLocalDateTime();
    }

    public static Long toEpochMilli(LocalDateTime time) {
        return time.atZone(ASIA_SEOUL)
                .toInstant()
                .toEpochMilli();
    }
}
