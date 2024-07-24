package com.hhplus.concert.adapter.controller.reservation;

import com.hhplus.concert.application.dto.request.reservation.ReservationRequest;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.ReservationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);
    @Autowired
    ReservationUseCase reservationUseCase;

    @PostMapping("/api/reservation")
    public ResponseEntity<ApiResponse> reservation(HttpServletRequest request,  @Valid @RequestBody ReservationRequest reservation) {
        String userId = request.getAttribute("userId").toString();
        ApiResponse response = reservationUseCase.reservation(userId , reservation.getConcertId() , reservation.getConcertDy() , reservation.getSeatNo());
        return ResponseEntity.ok(response);
    }
}
