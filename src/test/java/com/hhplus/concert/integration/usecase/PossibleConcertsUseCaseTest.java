package com.hhplus.concert.integration.usecase;

import com.hhplus.concert.adapter.repository.concert.ConcertRepository;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.PossibleConcertsUseCase;
import com.hhplus.concert.domain.model.concert.Concert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/*
 * 예약 가능한 콘서트 목록 조회 통합테스트
 *  성공 - 예약 가능 콘서트 목록 조회
 *  성공 - 예약 가능 콘서트 목록 없음
 */

@SpringBootTest
public class PossibleConcertsUseCaseTest {

    @Autowired
    PossibleConcertsUseCase possibleConcertsUseCase;
    @Autowired
    ConcertRepository concertRepository;



    @Test
    @DisplayName("성공-예약가능콘서트목록조회")
    public void 예약가능한_콘서트_목록_조회() {

        //given

        //when
        ApiResponse response = possibleConcertsUseCase.getPossibleConcerts();

        List<Concert> concerts = (List<Concert>)response.getData();

        //then
        Assertions.assertThat(concerts).extracting(Concert::getConcertId).contains(1L , 2L);

    }

    @Test
    @DisplayName("성공-예약가능한 콘서트 목록 없음")
    public void 예약_가능한_콘서트_없음() {

        //given

        //when
        concertRepository.deleteAll();
        ApiResponse response = possibleConcertsUseCase.getPossibleConcerts();

        //then
        Assertions.assertThat(response.getData()).isEqualTo(new ArrayList<>());

    }
}
