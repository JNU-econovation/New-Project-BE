package com.econo_4factorial.newproject.travel.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.common.exception.CommonErrorType;
import com.econo_4factorial.newproject.common.exception.InternalServerException;
import com.econo_4factorial.newproject.common.util.webSocket.WebSocketAuthService;
import com.econo_4factorial.newproject.common.util.webSocket.WebSocketResponser;
import com.econo_4factorial.newproject.travel.TravelEvent;
import com.econo_4factorial.newproject.travel.dto.Payload;
import com.econo_4factorial.newproject.travel.dto.res.TravelEventResponse;
import com.econo_4factorial.newproject.travel.dto.res.TravelEventResponseData;
import com.econo_4factorial.newproject.travel.exception.TravelErrorType;
import com.econo_4factorial.newproject.travel.service.TravelService;
import com.econo_4factorial.newproject.travel.util.PayloadMapper;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
class WebSocketHandlerTest {

    @Mock
    private PayloadMapper payloadMapper;

    @Mock
    private TravelService travelService;

    @Mock
    private WebSocketResponser responser;

    @Mock
    private WebSocketAuthService webSocketAuthService;

    @Mock
    private WebSocketSession session;

    private WebSocketHandler webSocketHandler;

    @BeforeEach
    void setUp() {
        webSocketHandler = new WebSocketHandler(payloadMapper, travelService, responser, webSocketAuthService);
        getClients().clear();
    }

    @Test
    void 인증_이벤트를_처리한다() throws Exception {
        Payload payload = new Payload("auth-user", Map.of("authorization", "Bearer token"));
        TextMessage message = new TextMessage("{\"event\":\"auth-user\"}");
        given(payloadMapper.fromMessage(message)).willReturn(payload);

        webSocketHandler.handleTextMessage(session, message);

        verify(webSocketAuthService).handleAuthEvent(payload, session);
        verify(responser).success(session, TravelEvent.AUTH, null);
        verifyNoInteractions(travelService);
    }

    @Test
    void 미인증_세션은_정책위반으로_종료한다() throws Exception {
        Payload payload = new Payload("start", Map.of());
        TextMessage message = new TextMessage("{\"event\":\"start\"}");
        given(payloadMapper.fromMessage(message)).willReturn(payload);
        given(webSocketAuthService.isAuthenticatedSession(session)).willReturn(false);
        given(session.getId()).willReturn("session-1");

        webSocketHandler.handleTextMessage(session, message);

        verify(session).close(CloseStatus.POLICY_VIOLATION);
        verifyNoInteractions(travelService, responser);
    }

    @Test
    void 인증된_세션은_산행서비스를_호출한다() throws Exception {
        Payload payload = new Payload("current-position", Map.of("latitude", 35.1));
        TextMessage message = new TextMessage("{\"event\":\"current-position\"}");
        TravelEventResponseData data = TravelEventResponseData.builder()
                .index(5)
                .isArrived(false)
                .isDeviation(false)
                .travelDistance(1.2)
                .remainTimeToStopover(120L)
                .remainTimeToEnd(300L)
                .build();
        TravelEventResponse response = new TravelEventResponse(TravelEvent.CURRENT_POSITION, data);
        given(payloadMapper.fromMessage(message)).willReturn(payload);
        given(webSocketAuthService.isAuthenticatedSession(session)).willReturn(true);
        given(session.getId()).willReturn("session-1");
        given(webSocketAuthService.getUserId("session-1")).willReturn(7L);
        given(travelService.execute(payload, 7L)).willReturn(response);

        webSocketHandler.handleTextMessage(session, message);

        verify(travelService).execute(payload, 7L);
        verify(responser).success(session, TravelEvent.CURRENT_POSITION, data);
    }

    @Test
    void 잘못된_요청예외가_발생하면_실패응답을_보낸다() throws Exception {
        Payload payload = new Payload("pause", Map.of());
        TextMessage message = new TextMessage("{\"event\":\"pause\"}");
        given(payloadMapper.fromMessage(message)).willReturn(payload);
        given(webSocketAuthService.isAuthenticatedSession(session)).willReturn(true);
        given(session.getId()).willReturn("session-1");
        given(webSocketAuthService.getUserId("session-1")).willReturn(3L);
        given(travelService.execute(payload, 3L))
                .willThrow(new BadRequestException(TravelErrorType.NOT_ALLOWED_EVENT_FOR_STATUS_EXCEPTION));

        webSocketHandler.handleTextMessage(session, message);

        verify(responser).fail(session, TravelErrorType.NOT_ALLOWED_EVENT_FOR_STATUS_EXCEPTION);
    }

    @Test
    void 내부서버예외가_발생하면_실패응답을_보낸다() throws Exception {
        Payload payload = new Payload("end", Map.of());
        TextMessage message = new TextMessage("{\"event\":\"end\"}");
        given(payloadMapper.fromMessage(message)).willReturn(payload);
        given(webSocketAuthService.isAuthenticatedSession(session)).willReturn(true);
        given(session.getId()).willReturn("session-1");
        given(webSocketAuthService.getUserId("session-1")).willReturn(3L);
        given(travelService.execute(payload, 3L))
                .willThrow(new InternalServerException(CommonErrorType.UN_EXPECTED_EXCEPTION));

        webSocketHandler.handleTextMessage(session, message);

        verify(responser).fail(session, CommonErrorType.UN_EXPECTED_EXCEPTION);
    }

    @Test
    void 예상치못한_예외가_발생하면_공통실패응답을_보낸다() throws Exception {
        TextMessage message = new TextMessage("{\"event\":\"start\"}");
        given(payloadMapper.fromMessage(message)).willThrow(new RuntimeException("boom"));

        webSocketHandler.handleTextMessage(session, message);

        verify(responser).fail(session, CommonErrorType.UN_EXPECTED_EXCEPTION);
    }

    @Test
    void 미인증_세션_종료중_IO예외가_발생하면_실패응답을_보낸다() throws Exception {
        Payload payload = new Payload("start", Map.of());
        TextMessage message = new TextMessage("{\"event\":\"start\"}");
        given(payloadMapper.fromMessage(message)).willReturn(payload);
        given(webSocketAuthService.isAuthenticatedSession(session)).willReturn(false);
        given(session.getId()).willReturn("session-1");
        org.mockito.Mockito.doThrow(new IOException("close fail"))
                .when(session)
                .close(CloseStatus.POLICY_VIOLATION);

        webSocketHandler.handleTextMessage(session, message);

        verify(responser).fail(session, CommonErrorType.WEB_SOCKET_IO_EXCEPTION);
    }

    @Test
    void 연결이_성공하면_세션을_저장한다() throws Exception {
        given(session.getId()).willReturn("session-1");

        webSocketHandler.afterConnectionEstablished(session);

        assertThat(getClients()).containsEntry("session-1", session);
    }

    @Test
    void 연결이_종료되면_세션과_산행상태를_정리한다() throws Exception {
        given(session.getId()).willReturn("session-1");
        getClients().put("session-1", session);
        given(webSocketAuthService.getUserId("session-1")).willReturn(9L);

        webSocketHandler.afterConnectionClosed(session, CloseStatus.NORMAL);

        assertThat(getClients()).doesNotContainKey("session-1");
        verify(travelService).deleteIfExistTravelTrackingInfo(9L);
        verify(webSocketAuthService).removeSession(session);
    }

    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<String, WebSocketSession> getClients() {
        return (ConcurrentHashMap<String, WebSocketSession>) ReflectionTestUtils.getField(WebSocketHandler.class,
                "clients");
    }
}
