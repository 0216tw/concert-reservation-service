package com.hhplus.concert.unit.service.concert;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/*
 1. 콘서트 예약 가능 날짜 조회 단위 테스트
       1-1) 성공 1 : 정상적으로 날짜가 조회된 경우
       1-2) 성공 2 : 예약 가능한 날짜가 없어 빈 리스트인 경우
 */
public class GetPossibleDatesTest extends ConcertServiceBase {

    @Test
    @DisplayName("성공-콘서트 예약 가능 날짜 조회")
    public void 콘서트_예약_가능_날짜_정상_조회() {

        //given
        long concertId = 1;  // 엔믹스
        List<String> possibleDates = List.of("20240710" , "20240711" , "20240712");

        //when
        when(concertScheduleRepository.getPossibleDates(anyLong())).thenReturn(possibleDates);
        List<String> dates = concertService.getPossibleDates(concertId);

        //then
        Assertions.assertThat(dates).isEqualTo(possibleDates);
    }

    @Test
    @DisplayName("성공-콘서트 예약 가능 날짜 없는 경우")
    public void 콘서트_예약_가능_날짜_없음() {
        //given
        long concertId = 1;  // 엔믹스
        List<String> possibleDates = new ArrayList<>(); //날짜없음

        //when
        when(concertScheduleRepository.getPossibleDates(anyLong())).thenReturn(possibleDates);
        List<String> dates = concertService.getPossibleDates(concertId);

        //then
        Assertions.assertThat(dates).isEqualTo(possibleDates);
    }
}
