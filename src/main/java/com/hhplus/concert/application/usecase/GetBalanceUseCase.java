package com.hhplus.concert.application.usecase;

import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.dto.response.user.ChargeResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component

public class GetBalanceUseCase {

    @Autowired
    UserService userService;

    public ApiResponse getBalance(String userId) {
        long balance = userService.getBalance(userId);
        HashMap<String, Object> data = new HashMap<>();
        data.put("balance", balance);
        return new ApiResponse("OK", MessageEnum.CHARGE_SUCCESS.getMessage(), data);
    }
}

