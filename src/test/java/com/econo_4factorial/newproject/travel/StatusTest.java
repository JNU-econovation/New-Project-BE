package com.econo_4factorial.newproject.travel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StatusTest {

    @Test
    void 상태가_기대하는_순서대로_정의되어_있다() {
        assertThat(Status.values()).containsExactly(
                Status.UNSTARTED,
                Status.STARTED,
                Status.TRAVEL,
                Status.PAUSED,
                Status.RESTARTED,
                Status.END
        );
    }

    @Test
    void 문자열로_상태를_조회할_수_있다() {
        assertThat(Status.valueOf("PAUSED")).isEqualTo(Status.PAUSED);
    }
}
