package com.hhplus.concert.unit.service.payment;

import com.hhplus.concert.adapter.gateway.PaymentGateway;
import com.hhplus.concert.adapter.repository.payment.PaymentRepository;
import com.hhplus.concert.adapter.repository.reservation.ReservationRepository;
import com.hhplus.concert.application.dto.response.payment.PaymentGatewayResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.payment.Payment;
import com.hhplus.concert.domain.model.reservation.Reservation;
import com.hhplus.concert.domain.service.payment.PaymentService;
import com.hhplus.concert.domain.service.reservation.ReservationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.parameters.P;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/*
  결제 기능 단위 테스트

  성공 - 유효한 예약번호로 결제 성공
  실패 1- 결제 실패

 */
public class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    PaymentGateway paymentGateway;

    @BeforeEach
    public void setUp() {

        paymentService = new PaymentService();
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("성공-유효한 예약번호로 결제")
    public void 유효한_예약번호로_결제() {
        //given
        long reservationId = 123456789L ;
        long ticketPrice = 150000L;
        Payment payment = new Payment(reservationId , new Date() , ticketPrice);

        //when
//      when(paymentGateway.pay(ticketPrice)).thenReturn(new PaymentGatewayResponse("200"));
        when(paymentRepository.save(payment)).thenReturn(payment);

//      paymentService.pay(ticketPrice);
        Payment paymentResult = paymentService.insertPaymentInfo(payment);

        //then
        Assertions.assertThat(paymentResult).isEqualTo(payment);

    }

    @Test
    @DisplayName("실패-결제에 실패한 경우")
    public void 결제실패() {

        //given
        long reservationId = 123456789L ;
        long ticketPrice = 150000L;
        String cardNo = "1111-1111-2222-2222";
        Payment payment = new Payment( reservationId , new Date()  , ticketPrice);

        //when
 //       when(paymentGateway.pay(cardNo, ticketPrice)).thenReturn(new PaymentGatewayResponse("ERR"));
        when(paymentRepository.save(payment)).thenThrow(new BusinessException("ERR" , MessageEnum.PAYMENT_FAILURE));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            paymentService.insertPaymentInfo(payment);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.PAYMENT_FAILURE.getMessage());
    }


}
