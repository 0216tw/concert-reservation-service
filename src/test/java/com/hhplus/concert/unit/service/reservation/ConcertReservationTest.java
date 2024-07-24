package com.hhplus.concert.unit.service.reservation;

import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.reservation.Reservation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/*
 * 좌석 예약 요청 단위 테스트
 *   성공 ) 예약 성공 및 결과 반환
 *   실패 1) 이미 다른 사람이 예약한 경우
 *   실패 2) 올바르지 않은 날짜값인 경우
 */
public class ConcertReservationTest extends ReservationServiceBase {
    @Test
    @DisplayName("성공-좌석 예약 성공 케이스")
    public void 좌석_예약_성공() {

        //given
        long concertId = 1L;
        String concertDy = "20240710";
        long seatNo = 3L;
        int result = 1;

        //when
        when(reservationRepository.updateSeatStatus(concertId, concertDy , seatNo, "AVAILABLE" , "ING")).thenReturn(1);

        //then
        Assertions.assertThat(result).isEqualTo(reservationService.updateSeatStatus(concertId,concertDy,seatNo,"AVAILABLE" , "ING")); //성공 행수 반환
    }

    @Test
    @DisplayName("실패-다른 사용자가 이미 예매한경우 (update = 0) ")
    public void 다른_사용자가_이미_예매한경우() {

        //given
        long concertId = 1L;
        String concertDy = "20240710";
        long seatNo = 3L;
        int result = 0;

        //when
        when(reservationRepository.updateSeatStatus(concertId, concertDy , seatNo , "AVAILABLE" , "ING")).thenReturn(0);

        //then
        Assertions.assertThat(result).isEqualTo(reservationService.updateSeatStatus(concertId,concertDy,seatNo, "AVAILABLE" , "ING")); //성공 행수 반환
    }

    @Test
    @DisplayName("실패-날짜값이 올바르지 않은 경우")
    public void 올바르지_않은_날짜값인경우() {

        //given
        long concertId = 1L;
        String concertDy = "2024071435250";
        long seatNo = 3L;
        int result = 1;

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            reservationService.updateSeatStatus(concertId , concertDy , seatNo , "AVAILABLE" , "ING");
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.INVALID_DATE_FORMAT.getMessage());
        verify(reservationRepository, never()).updateSeatStatus(anyLong() , anyString() , anyLong() , anyString() , anyString());
    }

    @Test
    @DisplayName("성공-유효한 예약번호인 경우")
    public void 유효한_예약번호() {

        //given
        long concertId = 1L;
        String concertDy = "20240710";
        long seatNo = 3L;
        int result = 1;
        String userId = "1111-1111-1111";

        Reservation reservation = new Reservation(concertId , concertDy , seatNo , userId);
        long reservationId = 123456789L;

        //when
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        Optional<Reservation> foundReservation = reservationRepository.findById(reservationId);

        //then
        Assertions.assertThat(reservation).isEqualTo(foundReservation.get());

    }

    @Test
    @DisplayName("실패-유효하지 않은 예약번호")
    public void 유효한_예약번호_아님() {

        long reservationId = 0000000011L;

        //when
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Reservation foundReservation = reservationService.getReservationInfo(reservationId);
        });

        //then
        Assertions.assertThat(exception.getMessage()).isEqualTo(MessageEnum.INVALID_RESERVATION_ID.getMessage());


    }
}
