package com.econo_4factorial.newproject.travel.util;

import com.econo_4factorial.newproject.travel.dto.Payload;
import com.econo_4factorial.newproject.travel.dto.req.StartEventReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PayloadMapperTest {

    private PayloadMapper payloadMapper;

    @BeforeEach
    void setUp() {
        payloadMapper = new PayloadMapper(new ObjectMapper());
    }

    @Test
    void 웹소켓_메시지를_Payload로_변환한다() throws JsonProcessingException {
        TextMessage message = new TextMessage("""
                {"event":"start","data":{"coordinate":[126.0,37.0],"courseId":1,"time":1000}}
                """);

        Payload payload = payloadMapper.fromMessage(message);

        assertThat(payload.event()).isEqualTo("start");
        assertThat(payload.data()).containsEntry("courseId", 1);
        assertThat(payload.data()).containsEntry("time", 1000);
    }

    @Test
    void payload의_data를_요청_DTO로_변환한다() {
        Payload payload = new Payload(
                "start",
                Map.of(
                        "coordinate", new double[]{126.0, 37.0},
                        "courseId", 1L,
                        "time", 1000L
                )
        );

        StartEventReq dto = payloadMapper.extractDataToDTO(payload, StartEventReq.class);

        assertThat(dto.courseId()).isEqualTo(1L);
        assertThat(dto.time()).isEqualTo(1000L);
        assertThat(dto.coordinate()).containsExactly(126.0, 37.0);
    }
}
