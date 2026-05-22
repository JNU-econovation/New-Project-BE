package com.econo_4factorial.newproject.travel.dto.res;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TravelEventResponseDataTest {

    @Test
    void 응답데이터를_빌더로_생성한다() {
        TravelEventResponseData result = TravelEventResponseData.builder()
                .index(4)
                .isArrived(true)
                .isDeviation(false)
                .travelDistance(3.8)
                .remainTimeToStopover(500L)
                .remainTimeToEnd(900L)
                .build();

        assertThat(result.getIndex()).isEqualTo(4);
        assertThat(result.getIsArrived()).isTrue();
        assertThat(result.getIsDeviation()).isFalse();
        assertThat(result.getTravelDistance()).isEqualTo(3.8);
        assertThat(result.getRemainTimeToStopover()).isEqualTo(500L);
        assertThat(result.getRemainTimeToEnd()).isEqualTo(900L);
    }
}
