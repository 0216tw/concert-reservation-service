package com.hhplus.concert.application.dto.response.payment;

public class PaymentGatewayResponse {
    private String status;

    public PaymentGatewayResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}