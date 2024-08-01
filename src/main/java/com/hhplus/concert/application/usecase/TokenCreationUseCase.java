package com.hhplus.concert.application.usecase;


import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.token.Token;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.token.TokenService;
import com.hhplus.concert.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TokenCreationUseCase {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final long WAIT_TIME_IN_MS = 10 * 1000; //n명당 10초

    @Transactional
    public ApiResponse createToken() {

        Token generatedToken = tokenService.createToken();
        String userId = generatedToken.getUserId();
        String token = generatedToken.getToken();
        String key = "waitingQueue";

        userService.insertUser(new User(userId, token, 0L));

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        double score = (double) Instant.now().toEpochMilli();
        zSetOps.add(key, token, score);
        long waitNo = zSetOps.rank(key , token);

        // 대기 시간 계산
        long totalWaitTimeInMs = (waitNo / 3000) * WAIT_TIME_IN_MS;
        long hours = TimeUnit.SECONDS.toHours(totalWaitTimeInMs);
        long minutes = TimeUnit.SECONDS.toMinutes(totalWaitTimeInMs) % 60;
        long seconds = totalWaitTimeInMs % 60;
        String waitTime = String.format("%02d시간 %02d분 %02d초", hours, minutes, seconds);
        Map<String, Object> data = Map.of("token", token, "waitNo", waitNo , "waitTime" , waitTime);

        return new ApiResponse("OK", MessageEnum.TOKEN_CREATEION_SUCCESS, data);

    }
}
