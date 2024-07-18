package com.hhplus.concert.integration.usecase;


import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.PossibleConcertSeatsUseCase;
import com.hhplus.concert.domain.model.queue.Queue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;


/*
 예약 가능한 좌석 조회 통합 테스트
 */
@SpringBootTest
public class PossibleConcertSeatsUseCaseTest {

    @Autowired
    PossibleConcertSeatsUseCase possibleConcertSeatsUseCase;

    @Test
    @DisplayName("성공-예약 가능 좌석 조회")
    public void 예약_가능_좌석_조회() {

        //given

        //when
        ApiResponse response = possibleConcertSeatsUseCase.getPossibleConcertSeats(1L, "20230710");
        List<Long> seats = (List<Long>) response.getData();

        //then
        Assertions.assertThat(seats).contains(11L , 12L , 15L , 17L , 18L);

    }

    @Test
    @DisplayName("성공-예약가능한 좌석이 없는 경우 ")
    public void 예약가능한_좌석이_없는_경우() {


        //given

        //when
        ApiResponse response = possibleConcertSeatsUseCase.getPossibleConcertSeats(1L, "20230725");
        List<Long> seats = (List<Long>) response.getData();

        //then
        Assertions.assertThat(seats.size()).isEqualTo(0);



    }
}
