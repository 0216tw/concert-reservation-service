package com.hhplus.concert.integration.usecase;

import com.hhplus.concert.adapter.repository.queue.QueueRepository;
import com.hhplus.concert.adapter.repository.user.UserRepository;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.ChargeUseCase;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.queue.Queue;
import com.hhplus.concert.domain.model.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/*
 * 잔액 충전 통합테스트
 * */
@SpringBootTest
public class ChargeUseCaseTest {

    @Autowired
    ChargeUseCase chargeUseCase;

    @Autowired
    TokenCreationUseCase tokenCreationUseCase;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("성공-잔액 충전에 성공")
    public void 잔액_충전_성공() {

        //given
        //토큰 생성
        HashMap<String , Object> data = (HashMap<String , Object>)tokenCreationUseCase.createToken().getData();
        String token = (String)data.get("token");
        long charge = 200000L;
        Optional<Queue> foundQueue = queueRepository.findByToken(token);
        String userId = foundQueue.get().getUserId();
        User user = userRepository.findById(userId).get();
        user.setBalance(user.getBalance() + charge);
        //when
        long afterBalance = userRepository.save(user).getBalance();

        //then
        Assertions.assertThat(afterBalance).isEqualTo(charge);
    }

    @Test
    @DisplayName("실패-존재하지 않는 사용자일경우")
    public void 사용자_없어서_잔액충전_실패() {

        String userId = "nouser";
        long charge = 200000L;

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ApiResponse response = chargeUseCase.charge(userId , charge);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.USER_NOT_FOUND.getMessage());

    }

}
