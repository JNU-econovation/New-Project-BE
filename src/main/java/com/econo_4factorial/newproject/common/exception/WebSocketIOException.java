package com.econo_4factorial.newproject.common.exception;

public class WebSocketIOException extends InternalServerException{
    public WebSocketIOException(){
        super(CommonErrorType.WEB_SOCKET_IO_EXCEPTION);
    }
}
