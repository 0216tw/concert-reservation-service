package com.hhplus.concert.integration.usecase;

import com.hhplus.concert.adapter.repository.queue.QueueRepository;
import com.hhplus.concert.adapter.repository.user.UserRepository;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.dto.response.token.TokenCreationResponse;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.queue.Queue;
import com.hhplus.concert.domain.model.token.Token;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.token.TokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


/*
 * 토큰 발급 통합테스트
 *  성공 - 토큰 발급 성공
 *  실패 - 대기열에는 추가되고 사용자 정보는 추가되지 않은 경우
 *  실패 - 토큰이 중복된 경우 (unique)
 */

@SpringBootTest
public class TokenCreationUseCaseTest {

    @Autowired
    TokenCreationUseCase tokenCreationUseCase;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    TokenService tokenService;

    @Test
    @DisplayName("사용자 토큰 발급에 성공한 경우")
    public void 사용자_토큰_발급에_성공한_경우() {

        //given

        //when
        ApiResponse apiResponse = tokenCreationUseCase.createToken();
        HashMap<String , Object> data = (HashMap<String, Object>)apiResponse.getData();
        String token = (String)data.get("token");
        long waitNo= (long)data.get("waitNo");

        Optional<User> user = userRepository.findById( tokenService.validateToken(token));
        Optional<Queue> queue = queueRepository.findByToken(token);

        //then
        Assertions.assertThat(user.isPresent()).isEqualTo(true);
        Assertions.assertThat(user.get().getToken()).isEqualTo(token);
        Assertions.assertThat(queue.isPresent()).isEqualTo(true);

    }


    @Test
    @DisplayName("실패- 토큰은 등록되었지만 사용자 등록에 실패한 경우")
    public void 토큰_등록_OK_하지만_사용자_등록_안됨() {

        //given

        //when
        ApiResponse apiResponse = tokenCreationUseCase.createToken();
        HashMap<String , Object> data = (HashMap<String, Object>)apiResponse.getData();
        String token = (String)data.get("token");
        long waitNo= (long)data.get("waitNo");


        String userId = tokenService.validateToken(token);
        userRepository.deleteById(userId);
        Optional<User> user = userRepository.findById(userId);
        Optional<Queue> queue = queueRepository.findByToken(token);

        //then
        Assertions.assertThat(user.isPresent()).isEqualTo(false);
        Assertions.assertThat(queue.isPresent()).isEqualTo(true);

    }
}




