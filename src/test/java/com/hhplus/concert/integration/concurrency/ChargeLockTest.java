package com.hhplus.concert.integration.concurrency;

import com.hhplus.concert.adapter.repository.user.UserRepository;
import com.hhplus.concert.application.concurrency.ChargeUseCaseConcurrency;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.ChargeUseCase;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.domain.model.token.Token;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.token.TokenService;
import com.hhplus.concert.domain.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/*
*  금액 충전 동시성 케이스 (모든 요청은 처리되어야 한다)
*  1. 낙관락 -> 재진입을 이용하면 가능
*  2. 비관락 -> 가능
*  3. 분산락 -> 가능
*/
public class ChargeLockTest extends LockTestBase {

    @Autowired
    ChargeUseCaseConcurrency chargeUseCaseConcurrency;

    User user ;

    @BeforeEach
    public void setUp() {
        //테스트용 사용자 생성 및 가져오기
        ApiResponse response = tokenCreationUseCase.createToken();
        HashMap<String , Object> data = (HashMap<String, Object>)response.getData();
        String token = (String)data.get("token");
        String userId = tokenService.validateToken(token);
        user = userService.findUserById(userId);
    }

    @AfterEach
    public void clear() {
        // userRepository.deleteById(user.getUserId());
    }

    @Test
    @DisplayName("낙관락-동시100회 충전 테스트")
    public void 낙관락_동시100회_충전_테스트() throws InterruptedException {
        int concurrentThreads = 100;
        long charge = 100000;
        int awaitSecond = 10;
        long expected = charge * concurrentThreads;

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentThreads);
        for (int i = 0; i < concurrentThreads; i++) {
            executorService.submit(() -> {
                chargeUseCaseConcurrency.chargeOptimisticLock(user.getUserId() , charge);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(awaitSecond, TimeUnit.SECONDS);
        Assertions.assertThat(userService.findUserById(user.getUserId()).getBalance()).isNotEqualTo(expected);
    }


    @Test
    @DisplayName("비관락-동시100회 충전 테스트")
    public void 비관락_동시100회_충전_테스트() throws InterruptedException {
        int concurrentThreads = 100;
        long charge = 100000;
        int awaitSecond = 10;
        long expected = charge * concurrentThreads;

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentThreads);
        for (int i = 0; i < concurrentThreads; i++) {
            executorService.submit(() -> {
                chargeUseCaseConcurrency.chargePessimisticLock(user.getUserId() , charge);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(awaitSecond, TimeUnit.SECONDS);
        Assertions.assertThat(userService.findUserById(user.getUserId()).getBalance()).isEqualTo(expected);
    }

    @Test
    @DisplayName("분산락-동시100회 테스트")
    public void 분산락_동시100회_충전_테스트() throws InterruptedException {
        int concurrentThreads = 100;
        long charge = 100000;
        long expected = charge * concurrentThreads;

        ExecutorService executorService = Executors.newFixedThreadPool(concurrentThreads);
        CountDownLatch latch = new CountDownLatch(concurrentThreads);
        for (int i = 0; i < concurrentThreads; i++) {
            executorService.submit(() -> {
                try {
                    chargeUseCaseConcurrency.chargeDistributedLock(user.getUserId() , charge);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Assertions.assertThat(userService.findUserById(user.getUserId()).getBalance()).isEqualTo(expected);
    }



}
