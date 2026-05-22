package com.econo_4factorial.newproject.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserAlertTest {

    @Test
    void 기본_알림값은_모두_true다() {
        UserAlert userAlert = new UserAlert();

        assertThat(userAlert.isEventAlert()).isTrue();
        assertThat(userAlert.isTravelDeviationAlert()).isTrue();
        assertThat(userAlert.isAccidentProneAreaAlert()).isTrue();
    }

    @Test
    void 알림설정을_수정한다() {
        UserAlert userAlert = new UserAlert();

        userAlert.updateAlerts(false, true, false);

        assertThat(userAlert.isEventAlert()).isFalse();
        assertThat(userAlert.isTravelDeviationAlert()).isTrue();
        assertThat(userAlert.isAccidentProneAreaAlert()).isFalse();
    }
}
