package com.hhplus.concert.unit.service.queue;

import com.hhplus.concert.domain.model.queue.Queue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

/*
2. 큐에 토큰 값 적재 단위테스트

   성공 : 큐 대기열 적재 성공
 */
public class InsertTokenToQueueTest extends QueueServiceBase {

    @Test
    @DisplayName("성공-대기열 적재 성공")
    public void 대기열_적재_성공() {

        //given
        String token = "0000-0000-0000-0000-token";
        String userId = "1111111L";

        Queue queue = new Queue(userId , token , "WAIT" , new Date() , null , null);

        //when
        when(queueRepository.save(queue)).thenReturn(queue);
        Queue savedQueue = queueRepository.save(queue);

        //then
        Assertions.assertThat(savedQueue).isEqualTo(queue);
        verify(queueRepository, times(1)).save(queue);
    }
}
