package com.econo_4factorial.newproject.travel.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.econo_4factorial.newproject.travel.Status;
import com.econo_4factorial.newproject.travel.TravelEvent;
import com.econo_4factorial.newproject.travel.exception.NotExistStatusException;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventPolicyTest {

    private EventPolicy eventPolicy;

    @BeforeEach
    void setUp() {
        eventPolicy = new EventPolicy();
    }

    @Test
    void 시작전_상태에서는_START만_허용한다() {
        허용된_이벤트만_통과한다(Status.UNSTARTED, Set.of(TravelEvent.START));
    }

    @Test
    void 시작_상태에서는_CURRENT_POSITION_END_PAUSE를_허용한다() {
        허용된_이벤트만_통과한다(Status.STARTED, Set.of(
                TravelEvent.CURRENT_POSITION,
                TravelEvent.END,
                TravelEvent.PAUSE
        ));
    }

    @Test
    void 이동중_상태에서는_CURRENT_POSITION_END_PAUSE를_허용한다() {
        허용된_이벤트만_통과한다(Status.TRAVEL, Set.of(
                TravelEvent.CURRENT_POSITION,
                TravelEvent.END,
                TravelEvent.PAUSE
        ));
    }

    @Test
    void 일시정지_상태에서는_RESTART_END_KEEP_ALIVE를_허용한다() {
        허용된_이벤트만_통과한다(Status.PAUSED, Set.of(
                TravelEvent.RESTART,
                TravelEvent.END,
                TravelEvent.KEEP_ALIVE
        ));
    }

    @Test
    void 재시작_상태에서는_CURRENT_POSITION_END를_허용한다() {
        허용된_이벤트만_통과한다(Status.RESTARTED, Set.of(
                TravelEvent.CURRENT_POSITION,
                TravelEvent.END
        ));
    }

    @Test
    void 종료_상태는_정책_검사_대상이_아니므로_예외를_던진다() {
        assertThatThrownBy(() -> eventPolicy.isAllowed(Status.END, TravelEvent.END))
                .isInstanceOf(NotExistStatusException.class);
    }

    private void 허용된_이벤트만_통과한다(Status status, Set<TravelEvent> allowedEvents) {
        Arrays.stream(TravelEvent.values())
                .forEach(event -> assertThat(eventPolicy.isAllowed(status, event))
                        .isEqualTo(allowedEvents.contains(event)));
    }
}
