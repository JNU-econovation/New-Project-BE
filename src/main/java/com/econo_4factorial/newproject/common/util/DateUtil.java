package com.econo_4factorial.newproject.common.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

@UtilityClass
public class DateUtil {
    private static int FIRST_DAY = 1;
    public static LocalDateTime getStartOfYearAndMonth (Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.atDay(FIRST_DAY).atStartOfDay();
    }

    public static LocalDateTime getEndOfYearAndMonth (Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
    }
}
