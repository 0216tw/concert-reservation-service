package com.hhplus.concert.application.concurrency;

import com.hhplus.concert.application.concurrency.charge.UserServiceConcurrency;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/* 동시성 테스트용*/
@Component
public class ChargeUseCaseConcurrency {

    @Autowired
    UserServiceConcurrency userServiceConcurrency;


    //낙관
    public ApiResponse chargeOptimisticLock(String userId, long charge) {

        long afterChargeBalance = userServiceConcurrency.chargeOptimisticLock(userId , charge);

        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , afterChargeBalance);
        return new ApiResponse("OK" , MessageEnum.CHARGE_SUCCESS.getMessage() , data );
    }

    //비관
    public ApiResponse chargePessimisticLock(String userId, long charge) {

        long afterChargeBalance = userServiceConcurrency.chargePessimisticLock(userId , charge);

        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , afterChargeBalance);
        return new ApiResponse("OK" , MessageEnum.CHARGE_SUCCESS.getMessage() , data );
    }

    //분산
    public ApiResponse chargeDistributedLock(String userId, long charge) {

        long afterChargeBalance = userServiceConcurrency.chargeDistributedLock(userId , charge);

        HashMap<String , Object> data = new HashMap<>();
        data.put("balance" , afterChargeBalance);
        return new ApiResponse("OK" , MessageEnum.CHARGE_SUCCESS.getMessage() , data );
    }




}
