package com.hhplus.concert.application.usecase;

import com.hhplus.concert.domain.service.queue.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetTokenStatusUseCase {

    @Autowired
    private QueueService queueService;

    public String getTokenStatus(String userId) {
        return queueService.getTokenStatus(userId);
    }
}
