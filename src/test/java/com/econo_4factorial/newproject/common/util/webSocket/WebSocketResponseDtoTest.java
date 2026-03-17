package com.econo_4factorial.newproject.common.util.webSocket;

import com.econo_4factorial.newproject.travel.TravelEvent;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketResponseDtoTest {

    @Test
    void 성공응답을_생성한다() {
        WebSocketSuccessRes response = WebSocketSuccessRes.ok(TravelEvent.START, Map.of("courseId", 3L));

        assertThat(response.event()).isEqualTo("start");
        assertThat(response.status()).isEqualTo("success");
        assertThat(response.data()).isEqualTo(Map.of("courseId", 3L));
    }

    @Test
    void 실패응답을_생성한다() {
        WebSocketFailRes response = WebSocketFailRes.fail("COMMON500_001", "예기치 못한 에러가 발생했습니다.");

        assertThat(response.status()).isEqualTo("fail");
        assertThat(response.errorCode()).isEqualTo("COMMON500_001");
        assertThat(response.message()).isEqualTo("예기치 못한 에러가 발생했습니다.");
    }
}
