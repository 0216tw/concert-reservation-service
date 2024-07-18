package com.hhplus.concert.domain.service.queue;

import com.hhplus.concert.adapter.repository.queue.QueueRepository;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.queue.Queue;
import com.hhplus.concert.domain.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class QueueService {

    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    @Autowired
    QueueRepository queueRepository;

    public long getWaitNo(String uuid) {
        long waitNo = queueRepository.getWaitNo(uuid);
        log.info("[대기열 WAIT 번호 반환] userId : {} , waitNo : {}" , uuid , waitNo);
        return waitNo;
    }

    public String getTokenStatus(String uuid) {
        String status = queueRepository.getTokenStatus(uuid);
        log.info("[토큰 상태 반환] token status : {}" , status);

        if(status == null) {
            log.warn("[토큰 상태 반환 실패] {} " , MessageEnum.TOKEN_STATUS_FAILURE.getMessage());
            throw new BusinessException("ERR" , MessageEnum.TOKEN_STATUS_FAILURE);
        }

        return status;
    }

    @Transactional
    public void insertQueue(Queue queue) {
        try {
            queueRepository.save(queue);
        } catch (DataIntegrityViolationException e) {
            log.error("[대기열 중복 오류 발생] {} , queueId : {} , userId : {} " , MessageEnum.TOKEN_DUPLICATE.getMessage() , queue.getQueueId() , queue.getUserId());
            throw new RuntimeException(MessageEnum.TOKEN_DUPLICATE.getMessage());
        }
    }

    public void removeExpiredTokens() {
        log.info("[EXPIRED 토큰 삭제]");
        queueRepository.removeExpiredTokens();
    }

    public void changeWaitToActiveTokens() {

        //이용 가능 대상을 50명으로 제한
        long activeCount = queueRepository.countActiveState();
        log.info("[WAIT -> ACTIVE 토큰 진입] 진입가능 토큰 수 : {}" , (50 - activeCount));
        queueRepository.changeWaitToActiveTokens((int)activeCount);
    }

    public void changeActiveToExpiredTokensByUserId(String userId) {
        log.info("[사용자 토큰 만료 ACTIVE -> EXPIRED 토큰 변환 (사용자)] userId : {}" , userId);
        queueRepository.changeActiveToExpiredTokensByUserId(userId);
    }

    public void changeActiveToExpiredTokensByActiveAt(long time) {
        log.info("[사용자 토큰 만료 ACTIVE -> EXPIRED 토큰 변환 (초) ] time : {} 초 이전 대상" , time);
        queueRepository.changeActiveToExpiredTokensByActiveAt(time);
    }



}
