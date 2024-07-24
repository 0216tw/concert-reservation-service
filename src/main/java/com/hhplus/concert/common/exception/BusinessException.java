package com.hhplus.concert.common.exception;

import com.hhplus.concert.common.constants.MessageEnum;
import org.springframework.boot.logging.LogLevel;

public class BusinessException extends RuntimeException {

    private final String code;
    private final String message;
    private LogLevel logLevel;

    private Object data;

    public BusinessException(String code , MessageEnum message) {
        this.code = code;
        this.message = message.getMessage();
    }

    public BusinessException(String code , MessageEnum message , Object data) {
        this.code = code;
        this.message = message.getMessage();
        this.data = data;
    }


    public BusinessException(String code , MessageEnum message , LogLevel logLevel) {
        this.code = code;
        this.message = message.getMessage();
        this.logLevel = logLevel;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
