package com.hhplus.concert.unit.service.concert;

import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.concert.ConcertSeat;
import com.hhplus.concert.domain.model.concert.ConcertSeatId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/*
 2. 콘서트 예약 가능 좌석 조회
       위치 : GetPossibleSeatsTest
       1-1) 성공 1 : 정상적으로 좌석이 조회된 경우
       1-2) 성공 2 : 예약 가능한 좌석이 없어 빈 리스트인 경우
       1-3) 실패 1 : 올바르지 않은 날짜값인 경우
 */
public class getPossibleSeatsTest extends com.hhplus.concert.unit.service.concert.ConcertServiceBase {

    @Test
    @DisplayName("성공-콘서트 예약 가능 좌석 조회")
    public void 콘서트_예약_가능_좌석_정상_조회() {

        //given
        long concertId = 1;
        String concertDy = "20240710";
        List<ConcertSeat> possibleSeats = List.of(
                new ConcertSeat(new ConcertSeatId(1L , "20230714", 5L), "VVIP" , 150000L , "AVAILABLE") ,
                new ConcertSeat(new ConcertSeatId(1L , "20230714", 7L), "VVIP" , 150000L , "AVAILABLE"),
                new ConcertSeat(new ConcertSeatId(1L , "20230714", 10L), "VVIP" , 150000L , "AVAILABLE"),
                new ConcertSeat(new ConcertSeatId(1L , "20230714", 12L), "VVIP" , 150000L , "AVAILABLE"));



                //when
        when(concertSeatRepository.getPossibleSeats(anyLong() , anyString())).thenReturn(possibleSeats);
        List<ConcertSeat> seats = concertService.getPossibleSeats(concertId , concertDy);

        //then
        Assertions.assertThat(seats).isEqualTo(possibleSeats);
    }

    @Test
    @DisplayName("성공-콘서트 예약 가능 좌석이 없는 경우")
    public void 콘서트_예약_가능_좌석_없음_빈리스트_반환() {

        //given
        long concertId = 1;
        String concertDy = "20240710";
        List<ConcertSeat> possibleSeats = List.of();

        //when
        when(concertSeatRepository.getPossibleSeats(anyLong() , anyString())).thenReturn(possibleSeats);
        List<ConcertSeat> seats = concertService.getPossibleSeats(concertId , concertDy);

        //then
        Assertions.assertThat(seats.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("실패-날짜값이 올바르지 않은 경우")
    public void 올바르지_않은_날짜값인경우() {

        //given
        long concertId = 1;
        String concertDy = "20240710123325346";
        List<Long> possibleSeats = List.of(1L,5L,7L,10L);

        //when
         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
             concertService.getPossibleSeats(concertId , concertDy);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.INVALID_DATE_FORMAT.getMessage());
        verify(concertSeatRepository, never()).getPossibleSeats(anyLong() , anyString());
    }


}
