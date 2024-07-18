package com.hhplus.concert.unit.service.concert;


import com.hhplus.concert.domain.model.concert.Concert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/*
 2. 예약 가능 콘서트 조회 단위 테스트
       1-1) 성공 1 : 정상적으로 콘서트 조회
       1-2) 성공 2 : 예약 가능한 콘서트 목록 없음
 */
public class GetPossibleConcertsTest extends ConcertServiceBase {

    @Test
    @DisplayName("성공-콘서트 목록 조회")
    public void 콘서트_목록_조회() {

        //등록시점
        Date date_20240710 = Date.from(Instant.parse("2024-07-10T00:00:00Z"));
        Date date_20240711 = Date.from(Instant.parse("2024-07-11T00:00:00Z"));

        List<Concert> concerts = List.of(
                new Concert(1L , "NMIXX콘서트" , 12 , date_20240710)
                , new Concert(2L , "데이식스콘서트" , 15 ,date_20240711 )
        );

        //when
        when(concertRepository.getPossibleConcerts()).thenReturn(concerts);
        List<Concert> foundConcerts = concertService.getPossibleConcerts();

        //then
        Assertions.assertThat(concerts).isEqualTo(foundConcerts);
    }


    @Test
    @DisplayName("성공-콘서트 목록 없음")
    public void 콘서트_목록_없음() {

        List<Concert> concerts = List.of();

        //when
        when(concertRepository.getPossibleConcerts()).thenReturn(concerts);
        List<Concert> foundConcerts = concertService.getPossibleConcerts();

        //then
        Assertions.assertThat(concerts).isEqualTo(foundConcerts);
    }

}
