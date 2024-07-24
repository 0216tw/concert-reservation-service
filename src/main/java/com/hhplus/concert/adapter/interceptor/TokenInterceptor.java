package com.hhplus.concert.adapter.interceptor;

import com.hhplus.concert.application.usecase.GetTokenStatusUseCase;
import com.hhplus.concert.application.usecase.GetWaitNoUseCase;
import com.hhplus.concert.application.usecase.TokenValidationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
    @Autowired
    TokenValidationUseCase tokenValidationUseCase;

    @Autowired
    GetTokenStatusUseCase getTokenStatusUseCase;

    @Autowired
    GetWaitNoUseCase getWaitNoUseCase;

    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {

        String token = request.getHeader("Authorization");

        if(token == null) {
            log.warn("[토큰 검증] 헤더에 토큰이 존재하지 않음");
            return false;
        }
        String userId = validateToken(token);
        String tokenStatus = getTokenStatus(userId);
        handleTokenStatus(tokenStatus, userId);

        return true;
    }

    private String validateToken(String token) throws BusinessException {
        String userId = tokenValidationUseCase.validate(token);
        if (userId == null) {
            throw new BusinessException("ERR", MessageEnum.TOKEN_VALIDATION_FAILURE);
        }
        return userId;
    }

    private String getTokenStatus(String userId) throws BusinessException {
        String tokenStatus = getTokenStatusUseCase.getTokenStatus(userId);
        if (tokenStatus == null) {
            throw new BusinessException("ERR", MessageEnum.TOKEN_STATUS_FAILURE);
        }
        return tokenStatus;
    }

    private void handleTokenStatus(String tokenStatus, String userId) throws BusinessException {
        if ("WAIT".equals(tokenStatus)) {
            HashMap<String, Long> data = new HashMap<>();
            data.put("waitNo", getWaitNoUseCase.getWaitNo(userId));
            throw new BusinessException("OK", MessageEnum.TOKEN_NOW_WAIT_STATUS, data);
        }

        if ("EXPIRED".equals(tokenStatus)) {
            throw new BusinessException("ERR", MessageEnum.TOKEN_EXPIRED);
        }
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
