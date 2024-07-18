package com.hhplus.concert.integration.usecase;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.PossibleConcertDatesUseCase;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;


/*
 예약 가능한 콘서트 날짜 조회 통합 테스트
 */
public class PossibleConcertDatesUseCaseTest {
    @Autowired
    PossibleConcertDatesUseCase possibleConcertDatesUseCase;

    @Test
    @DisplayName("성공-예약 가능 일자 조회")
    public void 예약_가능_일자_조회() {

        //given

        //when
        ApiResponse response = possibleConcertDatesUseCase.getPossibleConcertDates(1L);
        List<String> concerts = (List<String>) response.getData();

        //then
        Assertions.assertThat(concerts).contains("20240711");
    }

    @Test
    @DisplayName("성공-예약가능한 콘서트 일자 없음")
    public void 예약가능한_콘서트_일자_없는_경우() {


        //given

        //when
        ApiResponse response = possibleConcertDatesUseCase.getPossibleConcertDates(8123L);
        List<String> concerts = (List<String>) response.getData();


        //then
        Assertions.assertThat(concerts.size()).isEqualTo(0);
    }


}
