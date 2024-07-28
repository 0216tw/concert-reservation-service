package com.hhplus.concert.integration.usecase;

import com.hhplus.concert.adapter.repository.queue.QueueRepository;
import com.hhplus.concert.adapter.repository.reservation.ReservationRepository;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.ReservationUseCase;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.queue.Queue;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class ConcertReservationConcurrencyTest {

    /*
     * 좌석 예약 동시성 통합테스트
     */

    @Autowired
    ReservationUseCase reservationUseCase;

    @Autowired
    TokenCreationUseCase tokenCreationUseCase;

    @Autowired
    UserService userService;
    @Autowired
    QueueRepository queueRepository;

    @Autowired
    ReservationRepository reservationRepository;

    User user;
    @BeforeEach
    public void setUp() {
        //given
        //사용자 생성
        HashMap<String, Object> data = (HashMap<String, Object>) tokenCreationUseCase.createToken().getData();
        String token = (String) data.get("token");
        Optional<Queue> foundQueue = queueRepository.findByToken(token);
        String userId = foundQueue.get().getUserId();
        user = userService.findUserById(userId);
    }

    @Test
    @DisplayName("성공-좌석 예약 성공")
    public void 좌석_예약_성공() throws InterruptedException {

        long concertId = 3L;
        String concertDy = "20230714";
        long seatNo = 16L;

        int concurrentThreads = 100;
        long charge = 100000;
        int awaitSecond = 10;

        AtomicInteger updateCount = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentThreads);
        CountDownLatch latch = new CountDownLatch(concurrentThreads);

        for (int i = 0; i < concurrentThreads; i++) {
            executorService.submit(() -> {

                try {
                    //when
                    ApiResponse response = reservationUseCase.reservation(user.getUserId(), concertId, concertDy, seatNo);
                    if(response.getData() != null) updateCount.addAndGet(1); //예약번호가 null이 아니면 예약성공
                } catch (Exception e) {

                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertThat(updateCount.get()).isEqualTo(1);

    }
}
