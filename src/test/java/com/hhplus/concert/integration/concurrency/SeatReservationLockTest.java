package com.hhplus.concert.integration.concurrency;

import com.hhplus.concert.adapter.repository.concert.ConcertSeatRepository;
import com.hhplus.concert.adapter.repository.reservation.ReservationRepository;
import com.hhplus.concert.adapter.repository.user.UserRepository;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.ChargeUseCase;
import com.hhplus.concert.application.usecase.ReservationUseCase;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.concert.ConcertService;
import com.hhplus.concert.domain.service.token.TokenService;
import com.hhplus.concert.domain.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatReservationLockTest extends LockTestBase {


    @Autowired
    ReservationUseCase reservationUseCase;
    User user ;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ConcertSeatRepository concertSeatRepository;

    @BeforeEach
    public void setUp() {
        //테스트용 사용자 생성 및 가져오기
        ApiResponse response = tokenCreationUseCase.createToken();
        HashMap<String , Object> data = (HashMap<String, Object>)response.getData();
        String token = (String)data.get("token");
        String userId = tokenService.validateToken(token);
        user = userService.findUserById(userId);

        reservationRepository.updateSeatStatus(3L, "20230714", 40L, "ING" , "AVAILABLE");
    }

    @AfterEach
    public void clear() {
        // userRepository.deleteById(user.getUserId());
    }

    public void lock없이_테스트(int concurrentThreads , int awaitSecond ,  int expected) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentThreads);
        AtomicInteger successCount = new AtomicInteger(0) ;
        AtomicInteger failureCount = new AtomicInteger(0) ;

        for (int i = 0; i < concurrentThreads; i++) {
            executorService.submit(() -> {
                try {
                    reservationUseCase.reservation(user.getUserId() , 3L , "20230714" , 41L);
                    successCount.addAndGet(1);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                    failureCount.addAndGet(1);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(awaitSecond, TimeUnit.SECONDS);
        Assertions.assertThat(failureCount.get()).isEqualTo(concurrentThreads-expected);
        Assertions.assertThat(successCount.get()).isEqualTo(expected);
    }

    @Test
    @DisplayName("lock없이 좌석 예약 요청")
    public void lock없이_좌석_예약_요청() throws InterruptedException {
        lock없이_테스트(500 , 20 ,  1);
    }


    @Test
    @DisplayName("분산락-동시500회 테스트")
    public void 분산락_동시500회_좌석_예약_요청() {
        //이 케이스에서는 분산락을 하지 않음
    }



}
