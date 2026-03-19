package com.econo_4factorial.newproject.common.util.webSocket;

import com.econo_4factorial.newproject.common.exception.CommonErrorType;
import com.econo_4factorial.newproject.travel.TravelEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WebSocketResponserTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private WebSocketResponser webSocketResponser;
    private WebSocketSession session;

    @BeforeEach
    void setUp() {
        webSocketResponser = new WebSocketResponser(objectMapper);
        session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("session-1");
    }

    @Test
    void 성공_메시지를_전송한다() throws Exception {
        webSocketResponser.success(session, TravelEvent.START, java.util.Map.of("courseId", 3L));

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session).sendMessage(captor.capture());

        JsonNode response = objectMapper.readTree(captor.getValue().getPayload());
        assertThat(response.get("event").asText()).isEqualTo("start");
        assertThat(response.get("status").asText()).isEqualTo("success");
        assertThat(response.get("data").get("courseId").asLong()).isEqualTo(3L);
    }

    @Test
    void 실패_메시지를_전송한다() throws Exception {
        webSocketResponser.fail(session, CommonErrorType.UN_EXPECTED_EXCEPTION);

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session).sendMessage(captor.capture());

        JsonNode response = objectMapper.readTree(captor.getValue().getPayload());
        assertThat(response.get("status").asText()).isEqualTo("fail");
        assertThat(response.get("errorCode").asText()).isEqualTo("COMMON500_001");
        assertThat(response.get("message").asText()).isEqualTo("예기치 못한 에러가 발생했습니다.");
    }
}
