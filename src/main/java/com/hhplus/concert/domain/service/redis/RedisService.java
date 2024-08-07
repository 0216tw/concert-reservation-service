package com.hhplus.concert.domain.service.redis;

import com.hhplus.concert.adapter.interceptor.TokenInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;


@Service
public class RedisService {

    @Autowired
    RedisTemplate<String , String> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);


    public void changeTokenFromWaitingToActive() {

        log.info("[토큰 waiting -> active 전환 스케줄러 실행] {} ", new Date());

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        String sourceKey = "waitingQueue";
        String destinationKey = "activeQueue";

        Set<String> tokens = zSetOps.range(sourceKey, 0, 2999);

        if (tokens != null && !tokens.isEmpty()) {
            double now = (double) System.currentTimeMillis();
            String expired = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            for (String token : tokens) {
                zSetOps.remove(sourceKey, token);
                zSetOps.add(destinationKey, token+":" +expired , now);
            }
        }
        log.info("[토큰 전환 결과] {} 건의 토큰이 activeQueue로 전환됨", tokens.size());
    }



    public void cleanUpExpiredTokens() {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        String destinationKey = "activeQueue";
        Set<String> tokens = zSetOps.range(destinationKey, 0, -1);
        if (tokens != null && !tokens.isEmpty()) {
            for (String token : tokens) {
                // 만료된 토큰을 제거하기 위해 token:시간 패턴의 키 생성

                String time = token.split(":")[1] ;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime tenMinutesAgo = now.minus(10, ChronoUnit.MINUTES);
                LocalDateTime tokenTime = LocalDateTime.parse(time, formatter);

                if(tokenTime.isBefore(tenMinutesAgo)) {
                    zSetOps.remove(destinationKey, token);
                    log.info("[active 토큰 시간 만료] : {}", token);
                }
            }
        }
    }


}
