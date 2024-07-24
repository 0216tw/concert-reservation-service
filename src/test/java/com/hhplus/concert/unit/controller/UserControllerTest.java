package com.hhplus.concert.unit.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.adapter.controller.user.UserController;
import com.hhplus.concert.adapter.interceptor.TokenInterceptor;
import com.hhplus.concert.application.dto.request.user.ChargeRequest;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.ChargeUseCase;
import com.hhplus.concert.application.usecase.GetBalanceUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.reservation.Reservation;
import com.hhplus.concert.domain.model.token.Token;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Token(토큰) 관련 API 기능 단위테스트
 * 1. 사용자 잔액 조회
 * 2. 사용자 잔액 충전
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ChargeUseCase chargeUseCase;

    @MockBean
    GetBalanceUseCase getBalanceUseCase;

    @MockBean
    TokenInterceptor tokenInterceptor;

    @BeforeEach
    public void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any() , any() , any())).thenReturn(true);
    }


    //잔액 충전 테스트
    @Test
    @DisplayName("성공-사용자 잔액 충전")
    public void 사용자_잔액_충전_성공() throws Exception{

        //given
        long balance = 150000;
        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , balance);

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.CHARGE_SUCCESS.getMessage())
                .data(data)
                .build();

        ChargeRequest chargeRequest = ChargeRequest.builder().charge(150000).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(chargeRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(chargeUseCase.charge(any(String.class) , any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .requestAttr("userId", 123456789L))
                .andExpect(content().json(expectedResponse));

        verify(chargeUseCase, times(1)).charge(any(String.class) , any(Long.class));
    }

    @Test
    @DisplayName("실패-사용자 잔액 충전")
    public void 사용자_잔액_충전_실패() throws Exception {

        //given
        long balance = 150000;
        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , balance);

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.CHARGE_FAILURE.getMessage())
                .data(data)
                .build();

        ChargeRequest chargeRequest = ChargeRequest.builder().charge(150000).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(chargeRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(chargeUseCase.charge(any(String.class) , any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .requestAttr("userId", 123456789L))
                .andExpect(content().json(expectedResponse));

        verify(chargeUseCase, times(1)).charge(any(String.class) , any(Long.class));

    }

    @Test
    @DisplayName("실패-충전값이 0이하인경우")
    public void 충전값이_0원_이하() throws Exception {

        //given
        long balance = 0;
        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , balance);

        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.NOT_CHARGE_UNDER_ZERO.getMessage())
                .build();

        ChargeRequest chargeRequest = ChargeRequest.builder().charge(balance).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(chargeRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(chargeUseCase.charge(any(String.class) , any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .requestAttr("userId", 123456789L))
                .andExpect(content().json(expectedResponse));

        verify(chargeUseCase, never()).charge(any(String.class) , any(Long.class));
    }

    @Test
    @DisplayName("실패-금액 파라미터가 누락된 경우")
    public void 금액_파라미터_없음() throws Exception {

        //given
        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.NOT_CHARGE_UNDER_ZERO.getMessage())
                .build();

        ChargeRequest chargeRequest = ChargeRequest.builder().build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(chargeRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(chargeUseCase.charge(any(String.class) , any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .requestAttr("userId", 123456789L))
                .andExpect(content().json(expectedResponse));

        verify(chargeUseCase, never()).charge(any(String.class) , any(Long.class));
    }

    //잔액 조회 테스트
    @Test
    @DisplayName("성공-잔액조회")
    public void 잔액_조회_성공() throws Exception {

        //given
        long balance = 170000L;
        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , balance);

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.SELECT_OK.getMessage())
                .data(data)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(getBalanceUseCase.getBalance(any(String.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(get("/api/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", 123456789L))
                .andExpect(content().json(expectedResponse));

        verify(getBalanceUseCase, times(1)).getBalance(any(String.class));

    }

    @Test
    @DisplayName("실패-사용자에 대한 잔액 정보 없음")
    public void 잔액_정보_없음() throws Exception {

        //given

        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.USER_NOT_FOUND.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(getBalanceUseCase.getBalance(any(String.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(get("/api/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", "123456789"))
                .andExpect(content().json(expectedResponse));

        verify(getBalanceUseCase, times(1)).getBalance(any(String.class));

    }
}
