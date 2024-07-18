package com.hhplus.concert.unit.service.queue;

import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/*
   토큰 상태 조회 단위테스트

   성공 : 토큰 상태에 따른 상태 반환
   실패 : 큐에 token이 없는 경우
 */
public class GetTokenStatusTest extends QueueServiceBase {

    @Test
    @DisplayName("성공-토큰 상태에 따른 상태 반환")
    public void WAIT이면_대기순서_반환() {

        //given
        String token = "0000-0000-0000-0000-token";
        String expectState = "WAIT";

        //when
        when(queueRepository.getTokenStatus(token)).thenReturn(expectState);
        String state = queueService.getTokenStatus(token);

        //then
        Assertions.assertThat(state).isEqualTo(expectState);
    }

    @Test
    @DisplayName("실패-큐에 token이 없는 경우")
    public void 큐에_토큰_없어서_상태값_못줌() {

        //given
        String token = "0000-0000-0000-0000-token";

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            String state = queueService.getTokenStatus(token);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.TOKEN_STATUS_FAILURE.getMessage());
        verify(queueRepository, times(1)).getTokenStatus(anyString());

    }
}











