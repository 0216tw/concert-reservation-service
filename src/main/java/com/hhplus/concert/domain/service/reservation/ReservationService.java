package com.hhplus.concert.domain.service.reservation;

import com.hhplus.concert.adapter.repository.reservation.ReservationRepository;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.concert.ConcertSeat;
import com.hhplus.concert.domain.model.reservation.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service

public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    ReservationRepository reservationRepository;

    public int updateSeatStatus(long concertId , String concertDy , long seatNo , String before , String after) {

        log.info("[좌석 예약 상태 변경]");
        //날짜 검증
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            LocalDate.parse(concertDy, formatter);
        } catch (DateTimeParseException e) {
            log.warn("[좌석 예약 상태 변경] {}" , MessageEnum.INVALID_DATE_FORMAT.getMessage());
            throw new BusinessException("ERR" , MessageEnum.INVALID_DATE_FORMAT);
        }

        return reservationRepository.updateSeatStatus(concertId , concertDy , seatNo , before , after);
    }

    @Transactional
    public long insertReservation(Reservation reservation) {

        long concertId = reservation.getConcertId();
        String concertDy = reservation.getConcertDy();
        long seatNo = reservation.getSeatNo();

        log.info("[예약 정보 등록 AVAILABLE -> ING] concertId : {} , concertDy : {} , seatNo : {}" , concertId , concertDy , seatNo);
        int result = reservationRepository.updateSeatStatus(concertId , concertDy , seatNo , "AVAILABLE" , "ING");
        if(result == 0) {
            log.warn("[예약 정보 등록 오류] {}" , MessageEnum.ALREADY_RESERVED.getMessage());
            throw new BusinessException("ERR" , MessageEnum.ALREADY_RESERVED);
        }
        return reservationRepository.save(reservation).getReservationId();
    }

    public int setNullTempReservationInfo(long reservationId) {
        log.info("[예약 정보 시간 초과] 예약번호 : {}" , reservationId);
        return reservationRepository.setNullTempReservationInfo(reservationId);
    }
    public Reservation getReservationInfo (long reservationId) {
        log.info("[예약 정보 조회] 예약번호 : {}" , reservationId);
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if(reservation.isEmpty()) {
            log.warn("[예약 정보 조회 오류] {}" , MessageEnum.INVALID_RESERVATION_ID.getMessage());
            throw new BusinessException("ERR" , MessageEnum.INVALID_RESERVATION_ID);
        }
        return reservation.get();
    }
}
