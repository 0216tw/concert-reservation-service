package com.hhplus.concert.unit.service.balance;

import com.hhplus.concert.domain.model.token.Token;
import com.hhplus.concert.domain.model.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.doAnswer;

/* 2. 잔액을 조회한다.
 *     파일 : GetBalanceTest
 *     실패 1) 해당하는 userId가 존재하지 않는 경우
 */
public class GetBalanceTest extends BalanceServiceBase {


    @Test
    @DisplayName("성공-잔액 조회")
    public void 잔액_조회_성공() {

        //given
        Token token = tokenService.createToken();
        String userId = token.getUserId();
        long charge = 150000L;
        //when
        doAnswer(invocation -> {
            return charge;
        }).when(balanceRepository).getBalance(userId);

        //when
        doAnswer(invocation -> {
            return Optional.of(new User());
        }).when(balanceRepository).findById(userId);

        long balance = balanceService.getBalance(userId);

        //then
        Assertions.assertThat(balance).isEqualTo(150000);
    }
}
