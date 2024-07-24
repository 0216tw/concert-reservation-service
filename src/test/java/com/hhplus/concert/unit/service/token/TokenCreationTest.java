package com.hhplus.concert.unit.service.token;

import com.hhplus.concert.domain.model.token.Token;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.when;

/* 토큰 생성 단위 테스트
* - 토큰 생성 성공한 경우
* - 토큰 생성 실패한 경우
*/
public class TokenCreationTest extends TokenServiceBase {

    @Test
    @DisplayName("성공-토큰 생성 성공")
    public void 토큰_생성_성공() {

        //given

        //when
        Token token = tokenService.createToken();


        //then
        Assertions.assertThat(token.getToken()).isNotNull();
    }

}
