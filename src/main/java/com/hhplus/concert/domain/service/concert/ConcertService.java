package com.hhplus.concert.domain.service.concert;

import com.hhplus.concert.adapter.repository.concert.ConcertRepository;
import com.hhplus.concert.adapter.repository.concert.ConcertScheduleRepository;
import com.hhplus.concert.adapter.repository.concert.ConcertSeatRepository;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.concert.Concert;
import com.hhplus.concert.domain.model.concert.ConcertSeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class ConcertService {

    private static final Logger log = LoggerFactory.getLogger(ConcertService.class);

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertScheduleRepository concertScheduleRepository;
    @Autowired
    private ConcertSeatRepository concertSeatRepository;



    public List<String> getPossibleDates(long concertId) {
        log.info("[예약가능한 좌석일자 조회] concertId : {}" , concertId);
        return concertScheduleRepository.getPossibleDates(concertId);
    }

    public List<ConcertSeat> getPossibleSeats(long concertId , String concertDy) throws RuntimeException {

        log.info("[예약가능한 좌석 조회] concertId : {} , concertDy : {} ", concertId , concertDy);

        //날짜 검증
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            LocalDate.parse(concertDy, formatter);
        } catch (DateTimeParseException e) {
            log.warn("[예약가능한 좌석일자 조회] {} " , MessageEnum.INVALID_DATE_FORMAT.getMessage());
            throw new RuntimeException(MessageEnum.INVALID_DATE_FORMAT.getMessage());
        }

        return concertSeatRepository.getPossibleSeats(concertId , concertDy);
    }

    public List<Concert> getPossibleConcerts() {
        log.info("[예약가능한 콘서트 목록 조회]");
        return concertRepository.getPossibleConcerts();
    }


}
