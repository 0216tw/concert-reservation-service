package com.hhplus.concert.application.dto.response.user;

public class ChargeResponse {
    private long balance;

    public ChargeResponse(long balance) {
        this.balance = balance;
    }

    public ChargeResponse() {
    }

    public long getBalance() {
        return balance;
    }
}
