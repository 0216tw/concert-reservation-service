package com.hhplus.concert.application.dto.response;

import com.hhplus.concert.common.constants.MessageEnum;
import lombok.Builder;

@Builder
public class ApiResponse {
    private String status;
    private String message;
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(String status, MessageEnum messageEnum, Object data) {
        this.status = status;
        this.message = messageEnum.getMessage();
        this.data = data;
    }

    public ApiResponse(String status, String message , Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String status, MessageEnum messageEnum) {
        this.status = status;
        this.message = messageEnum.getMessage();
    }

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
