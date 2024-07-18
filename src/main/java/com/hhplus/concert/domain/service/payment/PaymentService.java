package com.hhplus.concert.domain.service.payment;

import com.hhplus.concert.adapter.gateway.PaymentGateway;
import com.hhplus.concert.adapter.repository.payment.PaymentRepository;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.dto.response.payment.PaymentGatewayResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.payment.Payment;
import com.hhplus.concert.domain.model.reservation.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    PaymentGateway paymentGateway;

    @Autowired
    PaymentRepository paymentRepository;

    public void pay(String cardNo , long money) {

      //  PaymentGatewayResponse pgResult = paymentGateway.pay(cardNo , money);

     //   if(!pgResult.getStatus().equals("200")) {
     //       //결제 실패 로깅 TODO
     //       throw new BusinessException("ERR" , MessageEnum.PAYMENT_FAILURE);
     //   }
    }

    public Payment insertPaymentInfo(Payment payment) {
        log.info("[결제 정보 저장]");
        return paymentRepository.save(payment);
    }


}
