package com.econo_4factorial.newproject.common.util.webSocket;

import com.econo_4factorial.newproject.common.exception.ErrorType;
import com.econo_4factorial.newproject.travel.TravelEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketResponser {
    private final ObjectMapper mapper;

    public void success(WebSocketSession session, TravelEvent event, Object data) throws IOException {
        WebSocketSuccessRes successResponse = WebSocketSuccessRes.ok(event, data);
        String successResponseJson = mapper.writeValueAsString(successResponse);
        session.sendMessage(new TextMessage(successResponseJson));
        log.info("웹소켓 메세지 전송 성공 : session = {}. successResponseJson = {}", session.getId(), successResponseJson);
    }

    public void fail(WebSocketSession session, ErrorType errorType) throws IOException {
        String errorCode = errorType.getErrorCode();
        String errorMessage = errorType.getMessage();

        WebSocketFailRes failResponse = WebSocketFailRes.fail(errorCode, errorMessage);
        String failResponseJson = mapper.writeValueAsString(failResponse);
        session.sendMessage(new TextMessage(failResponseJson));
        log.info("웹소켓 메세지 전송 성공 : session = {}. failResponse = {}", session.getId(), failResponseJson);
    }
}
