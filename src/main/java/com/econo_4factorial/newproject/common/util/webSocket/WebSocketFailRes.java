package com.econo_4factorial.newproject.common.util.webSocket;

public record WebSocketFailRes(
        String status,
        String errorCode,
        String message
){
    public static WebSocketFailRes fail(String errorCode, String message) {
        return new WebSocketFailRes("fail", errorCode, message);
    }
}

