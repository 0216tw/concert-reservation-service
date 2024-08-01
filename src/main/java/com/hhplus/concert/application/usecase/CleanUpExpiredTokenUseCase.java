package com.hhplus.concert.application.usecase;

import com.hhplus.concert.domain.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CleanUpExpiredTokenUseCase {
    final static Logger log = LoggerFactory.getLogger(QueuePollingUseCase.class);

    @Autowired
    RedisService redisService;

    public void cleanUpExpiredTokens() {
        redisService.cleanUpExpiredTokens();
    }
}
