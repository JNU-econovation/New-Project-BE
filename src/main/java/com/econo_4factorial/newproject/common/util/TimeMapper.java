package com.econo_4factorial.newproject.common.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class TimeMapper {

    public static LocalDateTime toLocalTime(Long time) {
        return Instant.ofEpochMilli(time)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
