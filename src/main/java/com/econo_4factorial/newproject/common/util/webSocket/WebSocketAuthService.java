package com.econo_4factorial.newproject.common.util.webSocket;

import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
import com.econo_4factorial.newproject.auth.jwt.service.JwtTokenProvider;
import com.econo_4factorial.newproject.common.exception.WebSocketIOException;
import com.econo_4factorial.newproject.travel.dto.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketAuthService {
    private final String AUTHORIZATION = "authorization";

    private static final ConcurrentHashMap<String, Long> authenticatedClients = new ConcurrentHashMap<>();

    private final AuthTokenService authTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    public void handleAuthEvent(Payload payload, WebSocketSession session) throws IOException {
        Long userId = authenticateUser(payload.data());
        updateToAuthenticated(session.getId(), userId);
        log.info("사용자 인증 성공. sessionId = {}, userId = {}", session.getId(), userId);
    }

    private Long authenticateUser(Map<String, Object> data) throws IOException {
        Long userId = jwtTokenProvider.getUserIdFromAccessToken((String) data.get(AUTHORIZATION));
        authTokenService.isLoggedIn(userId, (String) data.get(AUTHORIZATION));
        return userId;
    }

    private void updateToAuthenticated(String sessionId, Long userId) {
        authenticatedClients.put(sessionId, userId);
    }

    public Boolean isAuthenticatedSession(WebSocketSession session) {
        return authenticatedClients.containsKey(session.getId());

    }

    public Long getUserId(String id) {
        return authenticatedClients.get(id);
    }

    public void removeSession(WebSocketSession session) {
        authenticatedClients.remove(session.getId());
    }
}

