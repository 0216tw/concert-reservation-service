package com.hhplus.concert.adapter.controller.payment;

import com.hhplus.concert.application.dto.request.payment.PaymentRequest;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.PaymentUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentUseCase paymentUseCase;

    @PostMapping("/api/payment")
    public ResponseEntity<ApiResponse> payment(@Valid @RequestBody PaymentRequest payment) {
        ApiResponse response = paymentUseCase.payment(payment.getReservationId());
       return ResponseEntity.ok(response);

    }
}
