package com.hhplus.concert.adapter.interceptor;

import com.hhplus.concert.application.usecase.TokenValidationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
    private static final long WAIT_TIME_IN_MS = 10*10000;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {

        String token = request.getHeader("Authorization");

        if(token == null) {
            log.warn("[토큰 검증] 헤더에 토큰이 존재하지 않음");
            return false;
        }

        return handleTokenStatus(token);
    }


    private boolean handleTokenStatus(String token) throws BusinessException {

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        //activeQueue에서 찾기
        Set<String> tokens = zSetOps.range("activeQueue", 0, -1);

        if(tokens != null && !tokens.isEmpty()) {


            for(String tok : tokens) {
                if(tok.contains(token)) {
                    log.info("[active토큰입니다]");
                    return true;
                }
            }
        }

        // waitingQueue에서 토큰 찾기
        Long waitNo = zSetOps.rank("waitingQueue", token);
        if (waitNo != null) {

            // 대기 시간 계산 (3초로 테스트)
            long totalWaitTimeInMs = (waitNo / 3) * WAIT_TIME_IN_MS;
            long hours = TimeUnit.SECONDS.toHours(totalWaitTimeInMs);
            long minutes = TimeUnit.SECONDS.toMinutes(totalWaitTimeInMs) % 60;
            long seconds = totalWaitTimeInMs % 60;
            String waitTime = String.format("%02d시간 %02d분 %02d초", hours, minutes, seconds);
            Map<String, Object> data = Map.of("token", token, "waitNo", waitNo , "waitTime" , waitTime);

            throw new BusinessException("OK", MessageEnum.TOKEN_NOW_WAIT_STATUS, data);
        }

        throw new BusinessException("ERR" , MessageEnum.TOKEN_EXPIRED);


    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 요청 후 처리 (예: 컨트롤러 실행 후)
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 요청 완료 후 처리 (예: 예외 처리, 리소스 해제)
    }

}
