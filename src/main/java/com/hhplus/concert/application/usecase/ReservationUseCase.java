package com.hhplus.concert.application.usecase;

import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.concert.ConcertSeat;
import com.hhplus.concert.domain.model.reservation.Reservation;
import com.hhplus.concert.domain.service.concert.ConcertService;
import com.hhplus.concert.domain.service.reservation.ReservationService;
import com.hhplus.concert.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component

public class ReservationUseCase {

    @Autowired
    ConcertService concertService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    UserService userService;

    @Transactional
    public ApiResponse reservation(String userId , long concertId , String concertDy , long seatNo) {

        //좌석 이용가능 여부 검증
        List<ConcertSeat> seats = concertService.getPossibleSeats(concertId , concertDy);
        ConcertSeat availableSeat = seats.stream().filter(seat -> seat.getId().getSeatNo() == seatNo).findFirst().orElseThrow(()->
                new BusinessException("ERR" , MessageEnum.IMPOSSIBLE_RESERVATION));

        //사용자 유무 검증
        userService.findUserById(userId);

        //좌석 예약 시도
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5); // 현재 시간에 5분을 더함

        Reservation reservation = new Reservation(concertId , concertDy , seatNo , availableSeat.getSeatGrade() , availableSeat.getPrice() , userId , calendar.getTime());

        long reservationId = reservationService.insertReservation(reservation);
        return new ApiResponse("OK" , MessageEnum.RESERVATION_OK , reservationId);
    }
}
