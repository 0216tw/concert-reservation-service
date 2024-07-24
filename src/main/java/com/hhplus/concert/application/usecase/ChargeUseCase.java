package com.hhplus.concert.application.usecase;

import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.dto.response.user.ChargeResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.service.user.UserService;
import org.hibernate.annotations.CollectionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ChargeUseCase {


    @Autowired
    UserService userService;

    public ApiResponse charge(String userId, long charge) {

        userService.findUserById(userId);
        long afterChargeBalance = userService.charge(userId , charge);

        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , afterChargeBalance);
        return new ApiResponse("OK" , MessageEnum.CHARGE_SUCCESS.getMessage() , data );
    }

}
