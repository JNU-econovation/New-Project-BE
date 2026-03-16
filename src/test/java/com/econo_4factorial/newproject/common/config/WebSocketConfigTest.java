package com.econo_4factorial.newproject.common.config;

import com.econo_4factorial.newproject.travel.handler.WebSocketHandler;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class WebSocketConfigTest {

    @Test
    void 웹소켓_핸들러를_등록한다() {
        WebSocketHandler webSocketHandler = mock(WebSocketHandler.class);
        WebSocketHandlerRegistry registry = mock(WebSocketHandlerRegistry.class);
        WebSocketHandlerRegistration registration = mock(WebSocketHandlerRegistration.class);
        WebSocketConfig webSocketConfig = new WebSocketConfig(webSocketHandler);

        given(registry.addHandler(webSocketHandler, "/travel-navigate")).willReturn(registration);
        given(registration.setAllowedOrigins("*")).willReturn(registration);

        webSocketConfig.registerWebSocketHandlers(registry);

        InOrder inOrder = inOrder(registry, registration);
        inOrder.verify(registry).addHandler(webSocketHandler, "/travel-navigate");
        inOrder.verify(registration).setAllowedOrigins("*");
    }
}
