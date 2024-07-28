package com.hhplus.concert.integration.concurrency;


import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.domain.service.token.TokenService;
import com.hhplus.concert.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
* 아래 동시성 케이스에 대한 LOCK 을 테스트합니다.
* 1. 좌석 결제 - 낙관락 사용
* 2. 좌석 예약 - 낙관락 사용
* 3. 금액 충전 - 비관락 사용
* 4. 분산락을 위한 추가 테스트 ) 만약 사용자의 금액이 1000만원이 있고, 700명이 동시에 만원씩 쓴다면? 1차 비관락 , 2차 분산락

* 테스트 방법 :
  케이스별 가능한 락에 대해 100회 , 500회 테스트로 진행합니다.
* */

@SpringBootTest
public class LockTestBase {

    @Autowired
    TokenCreationUseCase tokenCreationUseCase;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;


}
