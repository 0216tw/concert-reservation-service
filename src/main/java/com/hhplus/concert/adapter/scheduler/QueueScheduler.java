package com.hhplus.concert.adapter.scheduler;

import com.hhplus.concert.application.usecase.CleanUpExpiredTokenUseCase;
import com.hhplus.concert.application.usecase.QueuePollingUseCase;
import com.hhplus.concert.domain.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class QueueScheduler {
    @Autowired
    QueuePollingUseCase queuePollingUseCase;

    @Autowired
    CleanUpExpiredTokenUseCase cleanUpExpiredTokenUseCase;

    @Scheduled(fixedRate = 10000)
    public void polling() {
        queuePollingUseCase.changeTokenFromWaitingToActive();
    }


    @Scheduled(fixedRate = 10000)
    public void cleanUpExpiredTokens() {cleanUpExpiredTokenUseCase.cleanUpExpiredTokens();}
}
