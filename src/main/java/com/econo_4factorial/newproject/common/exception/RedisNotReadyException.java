package com.econo_4factorial.newproject.common.exception;

public class RedisNotReadyException extends InternalServerException{
    public RedisNotReadyException () {
        super(CommonErrorType.REDIS_NOT_READY_EXCEPTION);
    }
}
