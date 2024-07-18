package com.hhplus.concert.integration.usecase;


import com.hhplus.concert.adapter.scheduler.QueueScheduler;
import com.hhplus.concert.application.usecase.QueuePollingUseCase;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.repositoryImpl.queue.QueueRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.Thread.sleep;

/*
 * 큐 polling 통합테스트
 */
@SpringBootTest
public class QueuePollingIntegrationTest {


    @Autowired
    private QueueScheduler queueScheduler;

    @Autowired
    QueuePollingUseCase queuePollingUseCase;

    @Autowired
    TokenCreationUseCase tokenCreationUseCase;

    @Autowired
    QueueRepositoryImpl queueRepositoryImpl;

    @Test
    @DisplayName("큐 polling 테스트")
    public void 큐_polling_테스트() throws InterruptedException {
        sleep(20000);
        queueScheduler.polling();
    }

    @Test
    @DisplayName("삭제 및 적재 테스트")
    public void 큐_기능_통합테스트() throws InterruptedException {

        sleep(10000);
        queuePollingUseCase.checkTokenSchedule();

    }


    /*
        시나리오
        1. 대기열 진입 100명 WAIT 발생
        2. 3초마다 대기열 확인 (EXPIRED -> 삭제 , WAIT -> 50 - ACTIVE 만큼 추가)
        3. 1초마다 ACTIVE 를 현재 기준 -1초로 EXPIRED 처리
     */
//    @Test
//    @DisplayName("큐 시나리오 테스트2")
//    public void 큐_시나리오_테스트() throws InterruptedException {
//
//        for(int i = 0 ; i<100; i++) {
//            tokenCreationUseCase.createToken(); //토큰 생성 (WAIT 100개)
//        }
//
//        queueScheduler.polling();
//        queueScheduler.makeExpiredPerOneSecond();
//        sleep(10000);

//    }
}
