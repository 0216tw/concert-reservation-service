package com.hhplus.concert.unit.service.balance;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        String userId = "123456789";

        //when
        doAnswer(invocation -> {
            mockUser.get(0).setBalance(mockUser.get(0).getBalance());
            return mockUser.get(0).getBalance();
        }).when(balanceRepository).getBalance(userId);

        long balance = balanceService.getBalance(userId);

        //then
        Assertions.assertThat(balance).isEqualTo(mockUser.get(0).getBalance());
    }
}
