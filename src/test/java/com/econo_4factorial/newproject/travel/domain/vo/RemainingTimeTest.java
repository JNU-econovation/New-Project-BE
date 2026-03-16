package com.econo_4factorial.newproject.travel.domain.vo;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RemainingTimeTest {

    @Test
    void 남은시간을_생성한다() {
        RemainingTime remainingTime = new RemainingTime(Duration.ofMinutes(15), Duration.ofMinutes(35));

        assertThat(remainingTime.getToStopover()).isEqualTo(Duration.ofMinutes(15));
        assertThat(remainingTime.getToDestination()).isEqualTo(Duration.ofMinutes(35));
    }

    @Test
    void 남은시간을_수정한다() {
        RemainingTime remainingTime = new RemainingTime(Duration.ofMinutes(15), Duration.ofMinutes(35));

        remainingTime.updateRemainTimeToStopover(Duration.ofMinutes(10));
        remainingTime.updateRemainTimeToDestination(Duration.ofMinutes(20));

        assertThat(remainingTime.getToStopover()).isEqualTo(Duration.ofMinutes(10));
        assertThat(remainingTime.getToDestination()).isEqualTo(Duration.ofMinutes(20));
    }
}
