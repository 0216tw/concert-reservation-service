package com.hhplus.concert.unit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.adapter.controller.token.TokenController;
import com.hhplus.concert.adapter.interceptor.TokenInterceptor;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.reservation.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
 * Token(토큰) 관련 API 기능 단위테스트
 * 1. 토큰 발급 요청
 *
 */
@WebMvcTest(TokenController.class)
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TokenCreationUseCase tokenCreationUseCase;

    @MockBean
    TokenInterceptor tokenInterceptor;

    @BeforeEach
    public void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any() , any() , any())).thenReturn(true);
    }
    @Test
    @DisplayName("성공-토큰 발급에 성공한 경우")
    public void 토큰_발급_성공() throws Exception {

        //given
        HashMap<String , Object> data = new HashMap<>();
        data.put("token" , "0000-0000-0000-0000");

        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.TOKEN_CREATEION_SUCCESS.getMessage())
                .data(data)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(tokenCreationUseCase.createToken()).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(tokenCreationUseCase, times(1)).createToken();
    }

    @Test
    @DisplayName("실패-토큰 발급에 실패한 경우")
    public void 토큰_발급_실패() throws Exception {

        //given

        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.TOKEN_CREATION_FAILURE.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(tokenCreationUseCase.createToken()).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(tokenCreationUseCase, times(1)).createToken();
    }
}
