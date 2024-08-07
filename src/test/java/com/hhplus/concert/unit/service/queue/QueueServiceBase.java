package com.hhplus.concert.unit.service.queue;

import com.hhplus.concert.adapter.repository.queue.QueueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class QueueServiceBase {


    @Mock
    public QueueRepository queueRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }
}
