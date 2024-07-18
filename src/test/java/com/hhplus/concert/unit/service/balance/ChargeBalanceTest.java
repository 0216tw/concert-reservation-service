package com.hhplus.concert.unit.service.balance;

import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/* 잔액 충전 단위 테스트
 *  성공 - 충전 후 잔액 반환
 *  실패 1- 존재하지 않는 사용자인 경우
 */
public class ChargeBalanceTest extends com.hhplus.concert.unit.service.balance.BalanceServiceBase {

    @Test
    @DisplayName("성공-잔액 충전 성공 후 잔액 반환")
    public void 잔액_충전_성공후_잔액_반환() {

        //given
        String userId = "111111111";
        long charge = 150000 ;

        //when
        doAnswer(invocation -> {
            mockUser.get(0).setBalance(mockUser.get(0).getBalance() + charge);
            return mockUser.get(0).getBalance() ;
        }).when(balanceRepository).charge(userId , charge);

        long afterCharge = balanceService.charge(userId , charge);

        //then
        Assertions.assertThat(afterCharge).isEqualTo(mockUser.get(0).getBalance());
    }

//    @Test
//    @DisplayName("실패-충전할 금액이 0이하인경우") //컨트롤러에서 검증
//    public void 충전_금액이_0이하인_경우() {}

    @Test
    @DisplayName("실패- 사용자가 존재하지 않는 경우")
    public void 없는_사용자라_충전할_수_없음() {

        //given
        String userId = "87654345";
        long charge = 150000 ;

        //when
        doAnswer(invocation -> {
            return null ;
        }).when(balanceRepository).charge(userId , charge);

        //when
        RuntimeException exception = assertThrows(BusinessException.class , () -> {
            long afterCharge = balanceService.charge(userId , charge);
        });


        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.USER_NOT_FOUND.getMessage());

    }


}

















