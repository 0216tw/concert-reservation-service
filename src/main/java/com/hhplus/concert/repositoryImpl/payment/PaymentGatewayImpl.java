package com.hhplus.concert.repositoryImpl.payment;

import com.hhplus.concert.adapter.gateway.PaymentGateway;
import com.hhplus.concert.application.dto.response.payment.PaymentGatewayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentGatewayImpl implements PaymentGateway {

    public PaymentGatewayResponse pay(String cardNo , long money) {

        //추후 PG와 연결 (일단 무조건 정상처리)
        return new PaymentGatewayResponse("200");
    }
}
