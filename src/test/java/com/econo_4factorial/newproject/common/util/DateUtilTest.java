package com.econo_4factorial.newproject.common.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

    @Test
    void 해당_월의_시작_시각을_반환한다() {
        LocalDateTime startOfMonth = DateUtil.getStartOfYearAndMonth(2024, 2);

        assertThat(startOfMonth).isEqualTo(LocalDateTime.of(2024, 2, 1, 0, 0));
    }

    @Test
    void 해당_월의_마지막_시각을_반환한다() {
        LocalDateTime endOfMonth = DateUtil.getEndOfYearAndMonth(2024, 2);

        assertThat(endOfMonth).isEqualTo(LocalDateTime.of(2024, 2, 29, LocalTime.MAX.getHour(), LocalTime.MAX.getMinute(), LocalTime.MAX.getSecond(), LocalTime.MAX.getNano()));
    }
}
