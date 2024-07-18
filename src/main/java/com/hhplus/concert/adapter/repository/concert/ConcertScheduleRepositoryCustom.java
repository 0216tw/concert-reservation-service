package com.hhplus.concert.adapter.repository.concert;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertScheduleRepositoryCustom {

    //예약가능한 콘서트 일자 조회
    public List<String> getPossibleDates(long concertId);
}
