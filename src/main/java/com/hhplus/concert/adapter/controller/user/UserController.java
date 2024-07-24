package com.hhplus.concert.adapter.controller.user;

import com.hhplus.concert.application.dto.request.user.ChargeRequest;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.dto.response.user.ChargeResponse;
import com.hhplus.concert.application.usecase.ChargeUseCase;
import com.hhplus.concert.application.usecase.GetBalanceUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    ChargeUseCase chargeUseCase;
    @Autowired
    GetBalanceUseCase getBalanceUseCase;

    @PostMapping("/api/charge")
    public ResponseEntity<ApiResponse> charge(HttpServletRequest request , @RequestBody ChargeRequest chargeRequest) {

        String userId = request.getAttribute("userId").toString();
        long balance = chargeRequest.getCharge();
        if(balance <= 0) throw new BusinessException("ERR" , MessageEnum.NOT_CHARGE_UNDER_ZERO);

        ApiResponse response = chargeUseCase.charge(userId, chargeRequest.getCharge());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/charge")
    public ResponseEntity<ApiResponse> findBalance(HttpServletRequest request) {

        String userId = request.getAttribute("userId").toString();
        ApiResponse response = getBalanceUseCase.getBalance(userId);
        return ResponseEntity.ok(response);
    }
}
