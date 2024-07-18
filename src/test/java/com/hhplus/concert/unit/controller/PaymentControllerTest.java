package com.hhplus.concert.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.adapter.controller.payment.PaymentController;
import com.hhplus.concert.adapter.interceptor.TokenInterceptor;
import com.hhplus.concert.application.dto.request.payment.PaymentRequest;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.PaymentUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.payment.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Payment(결제) 관련 API 기능 단위테스트
 * 1. 예약한 내역 결제 요청
 *
 */

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PaymentUseCase paymentUseCase;

    @MockBean
    TokenInterceptor tokenInterceptor ;
    @BeforeEach
    public void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any() , any() , any())).thenReturn(true);
    }

    @Test
    @DisplayName("성공-결제에 성공한 경우")
    public void 요청에_대한_결제_성공() throws Exception {

        //given
        ApiResponse apiResponse = ApiResponse.builder()
                .status("OK")
                .message(MessageEnum.PAYMENT_SUCCESS.getMessage())
                .build();

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setReservationId(123456789);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(paymentRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(paymentUseCase.payment(any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(paymentUseCase, times(1)).payment(any(Long.class));
    }

    @Test
    @DisplayName("실패-결제에 실패한 경우")
    public void 요청에_대한_결제_실패() throws Exception {
        //given
        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.PAYMENT_FAILURE.getMessage())
                .build();

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setReservationId(123456789);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(paymentRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        //when
        when(paymentUseCase.payment(any(Long.class))).thenReturn(apiResponse);

        //then
        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))

                .andExpect(content().json(expectedResponse));

        verify(paymentUseCase, times(1)).payment(any(Long.class));
    }

    @Test
    @DisplayName("실패-파라미터 예약번호를 누락한 경우")
    public void 결제_요청_파라미터_누락() throws Exception {
        //given
        ApiResponse apiResponse = ApiResponse.builder()
                .status("ERR")
                .message(MessageEnum.BAD_REQUEST.getMessage())
                .build();

        PaymentRequest paymentRequest = new PaymentRequest();
         ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(paymentRequest);
        String expectedResponse = objectMapper.writeValueAsString(apiResponse);

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));
    }

}
