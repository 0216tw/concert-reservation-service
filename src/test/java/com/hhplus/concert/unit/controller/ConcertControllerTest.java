package com.hhplus.concert.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.adapter.controller.concert.ConcertController;
import com.hhplus.concert.adapter.interceptor.TokenInterceptor;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.PossibleConcertDatesUseCase;
import com.hhplus.concert.application.usecase.PossibleConcertSeatsUseCase;
import com.hhplus.concert.application.usecase.PossibleConcertsUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.concert.Concert;
import com.hhplus.concert.domain.service.concert.ConcertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
* Concert 관련 API 기능 단위테스트

* 1. 예약 가능한 콘서트 조회 API
* 2. 예약 가능한 날짜 조회 API
* 3. 예약 가능한 좌석 조회 API
*
*/
@WebMvcTest(ConcertController.class)
public class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PossibleConcertsUseCase possibleConcertsUseCase;

    @MockBean
    PossibleConcertDatesUseCase possibleConcertDatesUseCase;

    @MockBean
    PossibleConcertSeatsUseCase possibleConcertSeatsUseCase;

    @MockBean
    TokenInterceptor tokenInterceptor ;


    @BeforeEach
    public void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    // 예약 가능 콘서트 조회 테스트
    @Test
    @DisplayName("성공-예약가능한 콘서트 목록 조회")
    public void 예약_가능한_콘서트_조회() throws Exception {

        //등록시점
        Date date_20240710 = Date.from(Instant.parse("2024-07-10T00:00:00Z"));
        Date date_20240711 = Date.from(Instant.parse("2024-07-11T00:00:00Z"));

        List<Concert> concerts = List.of(
                new Concert(1L , "NMIXX콘서트" , 12 , date_20240710)
                , new Concert(2L , "데이식스콘서트" , 15 ,date_20240711 )
        );

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.SELECT_OK.getMessage())
                .data(concerts)
                .build();

        //when
        when(possibleConcertsUseCase.getPossibleConcerts()).thenReturn(apiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = "{\"status\":\"OK\",\"message\":\"조회완료\",\"data\":[{\"concertId\":1,\"concertName\":\"NMIXX콘서트\",\"age\":12,\"createdAt\":\"2024-07-10T00:00:00.000+00:00\"},{\"concertId\":2,\"concertName\":\"데이식스콘서트\",\"age\":15,\"createdAt\":\"2024-07-11T00:00:00.000+00:00\"}]}";

        //then
        mockMvc.perform(get("/api/concerts"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @DisplayName("성공-예약가능한 콘서트가 없는 경우")
    public void 예약_가능한_콘서트_없음() throws Exception {

        //given
        HashMap<String , Object> data = new HashMap<>();
        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.SELECT_OK.getMessage())
                .data(new ArrayList<>())
                .build();

        //when
        when(possibleConcertsUseCase.getPossibleConcerts()).thenReturn(apiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = "{\"status\":\"OK\",\"message\":\"조회완료\",\"data\":[]}";

        //then
        mockMvc.perform(get("/api/concerts"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }


    //예약 가능 콘서트 일자 조회 테스트
    @Test
    @DisplayName("성공-예약가능한 콘서트 일자 조회")
    public void 예약_가능한_콘서트_일자_조회() throws Exception {

        //given
        List<String> concertSchedules = List.of("20240801" , "20240802" , "20240803");
        HashMap<String , Object> data = new HashMap<>();
        data.put("schedules" , concertSchedules);

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.SELECT_OK.getMessage())
                .data(data)
                .build();

        // when
         when(possibleConcertDatesUseCase.getPossibleConcertDates(any(Long.class))).thenReturn(apiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        // then
        mockMvc.perform(get("/api/concerts/1/dates"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @DisplayName("성공-예약가능한 콘서트 일자 없는 경우")
    public void 예약_가능한_콘서트_일자_없음() throws Exception {

        //given
        List<String> concertSchedules = new ArrayList<>();
        HashMap<String , Object> data = new HashMap<>();
        data.put("schedules" , concertSchedules);

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.SELECT_OK.getMessage())
                .data(data)
                .build();

        // when
        when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(possibleConcertDatesUseCase.getPossibleConcertDates(any(Long.class))).thenReturn(apiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        // then
        mockMvc.perform(get("/api/concerts/1/dates"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @DisplayName("실패-파라미터를 입력하지 않은 경우")
    public void 파라미터_concertId_입력을_안한_경우() throws Exception {
        // when
        when(possibleConcertDatesUseCase.getPossibleConcertDates(any(Long.class))).thenReturn(null);

        // then
        mockMvc.perform(get("/api/concerts//dates")).andExpect(status().isNotFound());
        verify(possibleConcertDatesUseCase, never()).getPossibleConcertDates(any(Long.class));
    }

    @Test
    @DisplayName("실패-파라미터 콘서트Id가 음수인 경우")
    public void 파라미터_concertId가_음수() throws Exception {

        // when
         when(possibleConcertDatesUseCase.getPossibleConcertDates(any(Long.class))).thenReturn(null);

        //then
        mockMvc.perform(get("/api/concerts/-123/dates"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(MessageEnum.BAD_REQUEST.getMessage()));
        verify(possibleConcertDatesUseCase, never()).getPossibleConcertDates(any(Long.class));
    }

    @Test
    @DisplayName("실패-long이 아니라 문자가 들어온 경우")
    public void 파라미터가_문자로_들어온_경우() throws Exception {

        // when
        when(possibleConcertDatesUseCase.getPossibleConcertDates(any(Long.class))).thenReturn(null);

        // then
        mockMvc.perform(get("/api/concerts/가갸거겨/dates"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(MessageEnum.BAD_REQUEST.getMessage()));

        verify(possibleConcertDatesUseCase, never()).getPossibleConcertDates(any(Long.class));


    }


    //예약 가능 콘서트 좌석 조회 테스트
    @Test
    @DisplayName("성공-콘서트 예약 가능한 좌석 조회")
    public void 콘서트_예약_가능_좌석_조회() throws Exception {

        //given
        List<Long> seats = List.of(1L,2L,3L,4L,5L);
        HashMap<String , Object> data = new HashMap<>();
        data.put("seats" , seats);

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.SELECT_OK.getMessage())
                .data(seats)
                .build();

        //when
        when(possibleConcertSeatsUseCase.getPossibleConcertSeats(any(Long.class) , any(String.class))).thenReturn(apiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = "{\"status\":\"OK\",\"message\":\"조회완료\",\"data\":[1,2,3,4,5]}";

        //then
        mockMvc.perform(get("/api/concerts/1/dates/20240720/seats"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(possibleConcertSeatsUseCase, times(1)).getPossibleConcertSeats(any(Long.class) , any(String.class));

    }

    @Test
    @DisplayName("성공-예약 가능한 좌석 없음")
    public void 콘서트_예약_가능_좌석_없음() throws Exception {

        //given
        List<Long> seats = new ArrayList<>();
        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.SELECT_OK.getMessage())
                .data(seats)
                .build();

        //when
        when(possibleConcertSeatsUseCase.getPossibleConcertSeats(any(Long.class) , any(String.class))).thenReturn(apiResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = "{\"status\":\"OK\",\"message\":\"조회완료\",\"data\":[]}";

        //then
        mockMvc.perform(get("/api/concerts/1/dates/20240720/seats"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(possibleConcertSeatsUseCase, times(1)).getPossibleConcertSeats(any(Long.class) , any(String.class));

    }

    @Test
    @DisplayName("실패-요청 파라미터가 누락된 경우")
    public void 요청_파라미터_누락() throws Exception {

        // when
        when(possibleConcertSeatsUseCase.getPossibleConcertSeats(any(Long.class) , any(String.class))).thenReturn(null);

        // then
        mockMvc.perform(get("/api/concerts//dates/20240720/seats")).andExpect(status().isNotFound());
        verify(possibleConcertSeatsUseCase, never()).getPossibleConcertSeats(any(Long.class) , any(String.class));
    }

}
