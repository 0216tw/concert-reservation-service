package com.hhplus.concert.application.usecase;

import com.hhplus.concert.domain.service.queue.QueueService;
import com.hhplus.concert.domain.service.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class QueuePollingUseCase {

    final static Logger log = LoggerFactory.getLogger(QueuePollingUseCase.class);
    @Autowired
    QueueService queueService;


    public void checkTokenSchedule() {

        log.info("토큰 스케줄러가 실행됩니다. {}" , new Date());
        queueService.removeExpiredTokens();
        queueService.changeWaitToActiveTokens();
    }

    public void updateActiveToExpiredToken() {
        queueService.changeActiveToExpiredTokensByActiveAt(5);
    }
}
