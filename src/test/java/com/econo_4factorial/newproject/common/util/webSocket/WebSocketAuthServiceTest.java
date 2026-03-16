package com.econo_4factorial.newproject.common.util.webSocket;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.LoggedOutTokenException;
import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
import com.econo_4factorial.newproject.auth.jwt.service.JwtTokenProvider;
import com.econo_4factorial.newproject.travel.dto.Payload;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class WebSocketAuthServiceTest {

    @Mock
    private AuthTokenService authTokenService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private WebSocketSession session;

    private WebSocketAuthService webSocketAuthService;

    @BeforeEach
    void setUp() {
        webSocketAuthService = new WebSocketAuthService(authTokenService, jwtTokenProvider);
        인증세션_저장소를_초기화한다();
    }

    @AfterEach
    void tearDown() {
        인증세션_저장소를_초기화한다();
    }

    @Test
    void 인증_이벤트를_처리하면_세션이_인증상태로_저장된다() throws Exception {
        Payload payload = new Payload("auth-user", Map.of("authorization", "access-token"));
        given(session.getId()).willReturn("session-1");
        given(jwtTokenProvider.getUserIdFromAccessToken("access-token")).willReturn(1L);
        given(authTokenService.isLoggedIn(1L, "access-token")).willReturn(true);

        webSocketAuthService.handleAuthEvent(payload, session);

        assertThat(webSocketAuthService.isAuthenticatedSession(session)).isTrue();
        assertThat(webSocketAuthService.getUserId("session-1")).isEqualTo(1L);
        verify(authTokenService).isLoggedIn(1L, "access-token");
    }

    @Test
    void 인증에_실패하면_세션이_저장되지_않는다() {
        Payload payload = new Payload("auth-user", Map.of("authorization", "invalid-token"));
        given(session.getId()).willReturn("session-2");
        given(jwtTokenProvider.getUserIdFromAccessToken("invalid-token")).willReturn(2L);
        given(authTokenService.isLoggedIn(2L, "invalid-token")).willThrow(new LoggedOutTokenException());

        assertThatThrownBy(() -> webSocketAuthService.handleAuthEvent(payload, session))
                .isInstanceOf(LoggedOutTokenException.class);

        assertThat(webSocketAuthService.isAuthenticatedSession(session)).isFalse();
        assertThat(webSocketAuthService.getUserId("session-2")).isNull();
    }

    @Test
    void 세션을_삭제하면_인증정보가_제거된다() {
        Payload payload = new Payload("auth-user", Map.of("authorization", "access-token"));
        given(session.getId()).willReturn("session-3");
        given(jwtTokenProvider.getUserIdFromAccessToken("access-token")).willReturn(3L);
        given(authTokenService.isLoggedIn(3L, "access-token")).willReturn(true);

        try {
            webSocketAuthService.handleAuthEvent(payload, session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        webSocketAuthService.removeSession(session);

        assertThat(webSocketAuthService.isAuthenticatedSession(session)).isFalse();
        assertThat(webSocketAuthService.getUserId("session-3")).isNull();
    }

    @SuppressWarnings("unchecked")
    private void 인증세션_저장소를_초기화한다() {
        ConcurrentHashMap<String, Long> authenticatedClients =
                (ConcurrentHashMap<String, Long>) ReflectionTestUtils.getField(WebSocketAuthService.class, "authenticatedClients");
        authenticatedClients.clear();
    }
}
