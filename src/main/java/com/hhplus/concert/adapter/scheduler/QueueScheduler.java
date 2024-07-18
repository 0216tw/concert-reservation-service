package com.hhplus.concert.adapter.scheduler;

import com.hhplus.concert.application.usecase.QueuePollingUseCase;
import com.hhplus.concert.domain.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QueueScheduler {

    @Autowired
    QueuePollingUseCase queuePollingUseCase;

    @Scheduled(fixedRate = 3000)
    public void polling() {
        queuePollingUseCase.checkTokenSchedule();
    }

    //test
    //@Scheduled(fixedRate = 1000)
    //public void makeExpiredPerOneSecond() {queuePollingUseCase.updateActiveToExpiredToken();}

}
