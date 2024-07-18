package com.hhplus.concert.unit.service.token;

import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.token.Token;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

/*
  토큰 검증 단위 테스트
*  - 토큰 검증 성공
*  - 실패 1: 토큰 검증 결과값 다름
*  - 실패 2: 유효한 토큰이 아님
*/
public class TokenValidationTest extends TokenServiceBase {

    @Test
    @DisplayName("성공-검증값과 원본이 동일한 경우")
    public void 토큰_검증_성공() {

        //when
        Token token = tokenService.createToken();

        //given
        String validatedUuid = tokenService.validateToken(token.getToken());

        //then
        Assertions.assertThat(validatedUuid).isEqualTo(token.getUserId());
    }

    @Test
    @DisplayName("실패-검증값과 원본이 다른 경우")
    public void 토큰_검증_실패_검증값과_원본이_다른경우() {

        //when
        Token token = tokenService.createToken();

        //given
        String validatedUuid = tokenService.validateToken(token.getToken());

        //then
        Assertions.assertThat(validatedUuid).isNotEqualTo("abcde"); //기존값과 다른 케이스
    }

    @Test
    @DisplayName("실패-유효하지 않은 토큰인 경우")
    public void 토큰_검증_실패_유효하지_않은_토큰() {

        //given
        Token token = tokenService.createToken();

        //when
        RuntimeException exception = assertThrows(RuntimeException.class , () -> {
            String validatedUuid = tokenService.validateToken(token.getToken() + "abcde");
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.TOKEN_VALIDATION_FAILURE.getMessage());
    }

}
