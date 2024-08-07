package com.hhplus.concert.application.usecase;

import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetBalanceUseCase {

    @Autowired
    UserService userService;

    public ApiResponse getBalance(String userId) {

        User user = userService.findUserById(userId);
        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , user.getBalance());
        return new ApiResponse("OK" , MessageEnum.CHARGE_SUCCESS.getMessage() , data );
    }

    //지금은 그냥 모든 클래스를 받을 수 있는 대상

}
