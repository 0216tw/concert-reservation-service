package com.hhplus.concert.application.usecase;

import com.hhplus.concert.domain.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class QueuePollingUseCase {

    final static Logger log = LoggerFactory.getLogger(QueuePollingUseCase.class);

    @Autowired
    RedisService redisService;

    private RedisTemplate<String, String> redisTemplate;


    public void changeTokenFromWaitingToActive() {
        redisService.changeTokenFromWaitingToActive();
    }
}
