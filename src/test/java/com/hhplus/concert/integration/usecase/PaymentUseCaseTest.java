package com.hhplus.concert.integration.usecase;

import com.hhplus.concert.adapter.repository.queue.QueueRepository;
import com.hhplus.concert.adapter.repository.reservation.ReservationRepository;
import com.hhplus.concert.adapter.repository.user.UserRepository;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.PaymentUseCase;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.queue.Queue;
import com.hhplus.concert.domain.model.reservation.Reservation;
import com.hhplus.concert.domain.model.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/*
* 결제 통합테스트
* */
@SpringBootTest
public class PaymentUseCaseTest {

    @Autowired
    TokenCreationUseCase tokenCreationUseCase;
    @Autowired
    PaymentUseCase paymentUseCase;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("성공-결제 프로세스 성공")
    public void 결제_프로세스_성공() {

        //given
        //토큰 생성
        HashMap<String , Object> data = (HashMap<String , Object>)tokenCreationUseCase.createToken().getData();
        String token = (String)data.get("token");
        Optional<Queue> foundQueue = queueRepository.findByToken(token);

        //잔액충전
        String userId = foundQueue.get().getUserId();
        User user = userRepository.findById(userId).get();
        user.setBalance(user.getBalance() + 150000);
        //when
        long afterBalance = userRepository.save(user).getBalance();


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5); // 현재 시간에 5분을 더함

        Reservation reservation = new Reservation(3L, "20230714", 5L , "VVIP" , 150000L ,userId
                , calendar.getTime());
        Reservation savedReservation = reservationRepository.save(reservation);
        long reservationId =savedReservation.getReservationId();

        //좌석 예약
        reservationRepository.updateSeatStatus(3L , "20230714" , 5L , "AVAILABLE" , "ING");

        //when
        ApiResponse response = paymentUseCase.payment(reservationId);

        //then
        Assertions.assertThat(response.getMessage()).isEqualTo(MessageEnum.PAYMENT_SUCCESS.getMessage());
    }


    @Test
    @DisplayName("실패-사용자가 존재하지 않는 경우")
    public void 사용자가_존재하지_않음() {

        //given
        //잔액충전
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5); // 현재 시간에 5분을 더함

        Reservation reservation = new Reservation(3L, "20230714", 5L , "VVIP" , 150000L ,"nouser"
                , calendar.getTime());
        Reservation savedReservation = reservationRepository.save(reservation);
        long reservationId =savedReservation.getReservationId();

        //좌석 예약
        reservationRepository.updateSeatStatus(3L , "20230714" , 5L , "AVAILABLE" , "ING");

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ApiResponse response = paymentUseCase.payment(reservationId);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("실패-잔액이 충분하지 않음")
    public void 잔액이_충분하지_않음() {

        //given
        //토큰 생성
        HashMap<String , Object> data = (HashMap<String , Object>)tokenCreationUseCase.createToken().getData();
        String token = (String)data.get("token");
        Optional<Queue> foundQueue = queueRepository.findByToken(token);
        String userId = foundQueue.get().getUserId();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5); // 현재 시간에 5분을 더함

        Reservation reservation = new Reservation(3L, "20230714", 5L , "VVIP" , 150000L ,userId
                , calendar.getTime());
        Reservation savedReservation = reservationRepository.save(reservation);
        long reservationId =savedReservation.getReservationId();

        //좌석 예약
        reservationRepository.updateSeatStatus(3L , "20230714" , 5L , "AVAILABLE" , "ING");




        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ApiResponse response = paymentUseCase.payment(reservationId);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.INSUFFICIENT_BALANCE.getMessage());
    }

    @Test
    @DisplayName("실패-예약 기간(5분)이 지난경우")
    public void 예약_제한시간_만료됨() {

        //given
        HashMap<String , Object> data = (HashMap<String , Object>)tokenCreationUseCase.createToken().getData();
        String token = (String)data.get("token");
        Optional<Queue> foundQueue = queueRepository.findByToken(token);
        String userId = foundQueue.get().getUserId();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Reservation reservation = new Reservation(3L, "20230714", 5L , "VVIP" , 150000L ,userId
                , calendar.getTime());
        Reservation savedReservation = reservationRepository.save(reservation);
        long reservationId =savedReservation.getReservationId();

        //좌석 예약
        reservationRepository.updateSeatStatus(3L , "20230714" , 5L , "AVAILABLE" , "ING");

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ApiResponse response = paymentUseCase.payment(reservationId);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.RESERVATION_TIME_EXCEEDED.getMessage());
    }

}
