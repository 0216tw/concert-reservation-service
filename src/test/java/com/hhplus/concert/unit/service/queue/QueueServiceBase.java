package com.hhplus.concert.unit.service.queue;

import com.hhplus.concert.adapter.repository.concert.ConcertScheduleRepository;
import com.hhplus.concert.adapter.repository.concert.ConcertSeatRepository;
import com.hhplus.concert.adapter.repository.queue.QueueRepository;
import com.hhplus.concert.domain.service.concert.ConcertService;
import com.hhplus.concert.domain.service.queue.QueueService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class QueueServiceBase {

    @InjectMocks
    public QueueService queueService;

    @Mock
    public QueueRepository queueRepository;

    @BeforeEach
    public void setUp() {
        queueService = new QueueService();
        MockitoAnnotations.openMocks(this);
    }
}
