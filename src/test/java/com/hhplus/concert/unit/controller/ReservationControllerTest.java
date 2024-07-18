package com.hhplus.concert.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.adapter.controller.reservation.ReservationController;
import com.hhplus.concert.adapter.interceptor.TokenInterceptor;
import com.hhplus.concert.application.dto.request.reservation.ReservationRequest;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.ReservationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.payment.Payment;
import com.hhplus.concert.domain.model.reservation.Reservation;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Reservation(예약) 관련 API 기능 단위테스트
 * 1. 콘서트 내역 예약 요청
 *
 */
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ReservationUseCase reservationUseCase ;

    @MockBean
    TokenInterceptor tokenInterceptor ;
    @BeforeEach
    public void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any() , any() , any())).thenReturn(true);

    }

    @Test
    @DisplayName("성공-예약에 성공한 경우")
    public void 좌석_예약_성공() throws Exception {

        //given
        long reservationId = 123456789L;
        HashMap<String , Object> data = new HashMap<>();
        data.put("reservationId" , reservationId);
        String userId = "1111-1111-1111";

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.RESERVATION_SUCCESS.getMessage())
                .data(data)
                .build();

        Date date_20240720 = Date.from(Instant.parse("2024-07-20T10:10:10Z"));
        Reservation reservation = new Reservation( 1L , "20240720", 5L, "VIP" , 1L , userId , date_20240720);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(reservation);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(reservationUseCase.reservation(any(String.class) , any(Long.class) , any(String.class) , any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .requestAttr("userId", 123456789L))
                .andExpect(content().json(expectedResponse));

        verify(reservationUseCase, times(1)).reservation(any(String.class) , any(Long.class) , any(String.class) , any(Long.class));
    }

    @Test
    @DisplayName("실패-예약시간이 만료된 경우")
    public void 좌석_예약시간_만료() throws Exception {

        //given
        String userId = "1111-1111-1111";

        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.RESERVATION_TIME_EXCEEDED.getMessage())
                .build();

        Date date_20240720 = Date.from(Instant.parse("2024-07-20T10:10:10Z"));
        Reservation reservation = new Reservation( 1L , "20240720", 5L, "VIP" , 1L , userId , date_20240720);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(reservation);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(reservationUseCase.reservation(any(String.class) , any(Long.class) , any(String.class) , any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", 123456789L)
                .content(requestJson))
                .andExpect(content().json(expectedResponse));

        verify(reservationUseCase, times(1)).reservation(any(String.class) , any(Long.class) , any(String.class) , any(Long.class));
    }

    @Test
    @DisplayName("실패-입력 파라미터 값이 누락된 경우") //일자 검증은 통테에서
    public void 예약_요청_파라미터_누락() throws Exception {

        //given
        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.BAD_REQUEST.getMessage())
                .build();

        ReservationRequest reservationRequest = new ReservationRequest( 1L , "", 5L );

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(reservationRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(reservationUseCase.reservation(any(String.class) ,any(Long.class) , any(String.class) , any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(content().json(expectedResponse));

        verify(reservationUseCase, never()).reservation(any(String.class) , any(Long.class) ,any(String.class) , any(Long.class));
    }

    @Test
    @DisplayName("실패-입력 파라미터 자체 누락") //일자 검증은 통테에서
    public void 예약_요청_파라미터_자체_누락() throws Exception {

        //given
        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.BAD_REQUEST.getMessage())
                .build();

        ReservationRequest reservationRequest = ReservationRequest.builder().concertId(1L).seatNo(5L).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(reservationRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(reservationUseCase.reservation(any(String.class) ,any(Long.class) , any(String.class) , any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(content().json(expectedResponse));

        verify(reservationUseCase, never()).reservation(any(String.class) ,any(Long.class) , any(String.class) , any(Long.class));
    }
}
