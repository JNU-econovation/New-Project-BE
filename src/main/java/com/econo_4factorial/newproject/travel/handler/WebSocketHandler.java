package com.econo_4factorial.newproject.travel.handler;

import com.econo_4factorial.newproject.common.exception.BadRequestException;
import com.econo_4factorial.newproject.common.exception.CommonErrorType;
import com.econo_4factorial.newproject.common.exception.InternalServerException;
import com.econo_4factorial.newproject.common.exception.WebSocketIOException;
import com.econo_4factorial.newproject.common.util.webSocket.WebSocketAuthService;
import com.econo_4factorial.newproject.common.util.webSocket.WebSocketResponser;
import com.econo_4factorial.newproject.travel.TravelEvent;
import com.econo_4factorial.newproject.travel.dto.Payload;
import com.econo_4factorial.newproject.travel.dto.res.TravelEventResponse;
import com.econo_4factorial.newproject.travel.service.TravelService;
import com.econo_4factorial.newproject.travel.util.PayloadMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final String AUTH_EVENT = "auth-user";

    private static final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    private final PayloadMapper payloadMapper;
    private final TravelService travelService;
    private final WebSocketResponser responser;
    private final WebSocketAuthService webSocketAuthService;

    @Override //추후 구현예정
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            Payload payload = ParsePayload(message);

            //사용자 인증 이벤트 과정
            if (isAuthUserEvent(payload.event())) {
                webSocketAuthService.handleAuthEvent(payload, session);
                responser.success(session, TravelEvent.AUTH, null);
                return;
            }

            //인증된 사용자 세션인지 확인
            if (!isAuthenticatedSession(session)) {
                log.info("미인증 사용자 세션 종료. sessionId = {}", session.getId());
                closeUnAuthenticatedSession(session);
                return;
            }

            //실시간 안내 과정
            Long userId = webSocketAuthService.getUserId(session.getId());
            TravelEventResponse responseMessage = travelService.execute(payload, userId);
            responser.success(session, responseMessage.getEvent(), responseMessage.getData());
        } catch (BadRequestException e) {
            log.warn("webSocket 메세지 처리 중 예외 발생: ", e);
            responser.fail(session, e.getErrorType());

        } catch (InternalServerException e) {
            log.warn("webSocket 메세지 처리 중 예외 발생: ", e);
            responser.fail(session, e.getErrorType());

        } catch (Exception e) {
            log.error("webSocket 메세지 처리 중 예상치 못한 에러 발생", e);
            responser.fail(session, CommonErrorType.UN_EXPECTED_EXCEPTION);
        }
    }

    private void closeUnAuthenticatedSession(WebSocketSession session) {
        try {
            session.close(CloseStatus.POLICY_VIOLATION);
        } catch (IOException e) {
            log.error("미인증 사용자 세션 종료 중 에러 발생. sessionId = {}", session.getId(), e);
            throw new WebSocketIOException();
        }
    }

    private boolean isAuthenticatedSession(WebSocketSession session) {
        return webSocketAuthService.isAuthenticatedSession(session);
    }

    private boolean isAuthUserEvent(String event) {
        return event.equals(AUTH_EVENT);
    }

    private Payload ParsePayload(TextMessage message) throws JsonProcessingException {
        return payloadMapper.fromMessage(message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        clients.put(session.getId(), session);
        log.info("Websocket 연결 성공. sessionId={}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.remove(session.getId());
        Long userId = webSocketAuthService.getUserId(session.getId());
        travelService.deleteIfExistTravelTrackingInfo(userId);
        webSocketAuthService.removeSession(session);
        log.info("Websocket 연결 종료. sessionId={}", session.getId());
    }
}
