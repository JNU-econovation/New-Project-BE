package com.econo_4factorial.newproject.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TimeMapperTest {

    @Test
    void 에포크_밀리초를_서울_시간_기준_LocalDateTime으로_변환한다() {
        LocalDateTime localDateTime = TimeMapper.toLocalTime(0L);

        assertThat(localDateTime).isEqualTo(LocalDateTime.of(1970, 1, 1, 9, 0));
    }
}
