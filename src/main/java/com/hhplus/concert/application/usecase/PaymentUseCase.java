package com.hhplus.concert.application.usecase;

import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.payment.Payment;
import com.hhplus.concert.domain.model.reservation.Reservation;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.payment.PaymentService;
import com.hhplus.concert.domain.service.queue.QueueService;
import com.hhplus.concert.domain.service.reservation.ReservationService;
import com.hhplus.concert.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class PaymentUseCase {

    @Autowired
    PaymentService paymentService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    UserService userService ;

    @Autowired
    QueueService queueService;

    @Transactional
    public ApiResponse payment(long reservationId) {

        //예약번호 검증
        Reservation reservation = reservationService.getReservationInfo(reservationId);
        long price = reservation.getPrice();

        //예약 기간이 현재 기준 5분 경과했을 경우 예약 취소 처리 및 오류 반환
        Date expiredAt = reservation.getTempReserveExpiredAt();

        if(expiredAt == null || expiredAt.before(new Date())) {
            reservationService.setNullTempReservationInfo(reservationId);
            throw new BusinessException("ERR" , MessageEnum.RESERVATION_TIME_EXCEEDED);
        }

        //사용자 검증
        User user = userService.findUserById(reservation.getTempReserveUserId());
        if(user.getBalance() < price) throw new BusinessException("ERR" , MessageEnum.INSUFFICIENT_BALANCE);

        //사용자 금액 변경
        userService.use(user.getUserId() , price);

        //결제 요청 (TODO 연동)
        //paymentService.pay(price);

        //결제 정보 등록
        Payment payment = new Payment(reservationId, new Date() , price);
        paymentService.insertPaymentInfo(payment);

        //토큰 만료
        queueService.changeActiveToExpiredTokensByUserId(user.getUserId());


        return new ApiResponse("OK" , MessageEnum.PAYMENT_SUCCESS);
    }
}
