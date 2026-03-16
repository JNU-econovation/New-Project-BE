package com.econo_4factorial.newproject.travel;

import com.econo_4factorial.newproject.travel.exception.NotExistEventException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TravelEventTest {

    @Test
    void 이벤트_이름으로_여행_이벤트를_조회한다() {
        assertThat(TravelEvent.fromName("auth-user")).isEqualTo(TravelEvent.AUTH);
        assertThat(TravelEvent.fromName("start")).isEqualTo(TravelEvent.START);
        assertThat(TravelEvent.fromName("current-position")).isEqualTo(TravelEvent.CURRENT_POSITION);
        assertThat(TravelEvent.fromName("pause")).isEqualTo(TravelEvent.PAUSE);
        assertThat(TravelEvent.fromName("keep-alive")).isEqualTo(TravelEvent.KEEP_ALIVE);
        assertThat(TravelEvent.fromName("restart")).isEqualTo(TravelEvent.RESTART);
        assertThat(TravelEvent.fromName("end")).isEqualTo(TravelEvent.END);
    }

    @Test
    void 존재하지_않는_이벤트_이름이면_예외가_발생한다() {
        assertThatThrownBy(() -> TravelEvent.fromName("unknown"))
                .isInstanceOf(NotExistEventException.class);
    }
}
