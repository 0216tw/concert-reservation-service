package com.hhplus.concert.application.usecase;


import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.dto.response.token.TokenCreationResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.queue.Queue;
import com.hhplus.concert.domain.model.token.Token;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.queue.QueueService;
import com.hhplus.concert.domain.service.token.TokenService;
import com.hhplus.concert.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

@Component
public class TokenCreationUseCase {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private QueueService queueService;

    @Transactional
    public ApiResponse createToken() {

        Token generatedToken = tokenService.createToken();
        String userId = generatedToken.getUserId();
        String token = generatedToken.getToken();
        String status = generatedToken.getStatus();
        Date createdAt = generatedToken.getCreatedAt();

        //<token to queue>
        Queue queue = new Queue(userId , token , status , createdAt);
        User user = new User(userId , token , 0L);

        userService.insertUser(user);
        queueService.insertQueue(queue);
        long waitNo = queueService.getWaitNo(userId);

        HashMap<String , Object> data = new HashMap<>();
        data.put("token" , token);
        data.put("waitNo" , waitNo);
        return new ApiResponse("OK" , MessageEnum.TOKEN_CREATEION_SUCCESS , data);
    }
}
