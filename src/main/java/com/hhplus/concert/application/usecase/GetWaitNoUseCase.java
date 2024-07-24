package com.hhplus.concert.application.usecase;

import com.hhplus.concert.domain.service.queue.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetWaitNoUseCase {
    @Autowired
    private QueueService queueService;
    public long getWaitNo(String userId) {
        return queueService.getWaitNo(userId);
    }
}
