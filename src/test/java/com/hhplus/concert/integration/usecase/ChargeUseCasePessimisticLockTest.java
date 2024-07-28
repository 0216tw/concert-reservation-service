package com.hhplus.concert.integration.usecase;

import com.hhplus.concert.application.concurrency.ChargeUseCaseConcurrency;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.token.TokenService;
import com.hhplus.concert.domain.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ChargeUseCasePessimisticLockTest {

    @Autowired
    ChargeUseCaseConcurrency chargeUseCaseConcurrency;

    @Autowired
    TokenCreationUseCase tokenCreationUseCase;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;
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
}
