package com.hhplus.concert.integration.usecase;

import com.hhplus.concert.adapter.repository.queue.QueueRepository;
import com.hhplus.concert.adapter.repository.reservation.ReservationRepository;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.ReservationUseCase;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.queue.Queue;
import com.hhplus.concert.domain.model.reservation.Reservation;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
* 좌석 예약 통합테스트
*/
@SpringBootTest
public class ConcertReservationTest {


    @Autowired
    ReservationUseCase reservationUseCase;

    @Autowired
    TokenCreationUseCase tokenCreationUseCase;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @DisplayName("성공-좌석 예약 성공")
    @Transactional
    public void 좌석_예약_성공() {

        //given
        //사용자 생성
        HashMap<String , Object> data = (HashMap<String , Object>)tokenCreationUseCase.createToken().getData();
        String token = (String)data.get("token");
        Optional<Queue> foundQueue = queueRepository.findByToken(token);
        String userId = foundQueue.get().getUserId();

        long concertId = 3L;
        String concertDy = "20230714";
        long seatNo = 5L;

        //when
        ApiResponse response = reservationUseCase.reservation(userId , concertId , concertDy , seatNo);
        long reservationId = (Long)response.getData();
        //then
        Assertions.assertThat(response.getMessage()).isEqualTo(MessageEnum.RESERVATION_OK.getMessage());
        Assertions.assertThat(reservationId).isEqualTo(reservationRepository.findById(reservationId).get().getReservationId());

    }

    @Test
    @DisplayName("실패-예약가능한 좌석이 아닐 경우")
    public void 예약_가능한_좌석_아님() {

        //given
        //사용자 생성
        HashMap<String , Object> data = (HashMap<String , Object>)tokenCreationUseCase.createToken().getData();
        String token = (String)data.get("token");
        Optional<Queue> foundQueue = queueRepository.findByToken(token);
        String userId = foundQueue.get().getUserId();

        long concertId = 3L;
        String concertDy = "20230714";
        long seatNo = 124512L;

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ApiResponse response = reservationUseCase.reservation(userId , concertId , concertDy , seatNo);
        });
        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.IMPOSSIBLE_RESERVATION.getMessage());
    }

    //동시성 테스트
    @Test
    @DisplayName("동시에 여러 사람이 동일한 좌석을 예약하려는 경우")
    public void 하나의_좌석을_동시에_예약하려는_경우() throws InterruptedException {

        // when
        final int threadCount = 5;
        final ExecutorService executorService = Executors.newFixedThreadPool(100);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for(int i=0; i<threadCount; i++) {

            long concertId = 3L;
            String concertDy = "20230714";
            long seatNo = 5;
            String token = (String)((HashMap<String , Object>)tokenCreationUseCase.createToken().getData()).get("token");
            String userId = queueRepository.findByToken(token).get().getUserId();

            Reservation reservation = new Reservation(concertId , concertDy , seatNo , userId);

            executorService.submit(()->{
                try{
                    reservationUseCase.reservation(userId , concertId , concertDy , seatNo);
                    successCount.addAndGet(1);
                } catch (BusinessException e) {
                    failCount.addAndGet(1);
                } finally{
                    countDownLatch.countDown();
                }
            });

        }
        countDownLatch.await();
        executorService.shutdown();

        //업데이트는 한번만 수행되어야 함
        Assertions.assertThat(successCount.get()).isEqualTo(1);
        Assertions.assertThat(failCount.get()).isEqualTo(4);
    }


    //TODO 동시성 케이스 더 넣어보기

}
