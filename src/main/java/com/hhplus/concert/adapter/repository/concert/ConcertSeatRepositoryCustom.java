package com.hhplus.concert.adapter.repository.concert;

import com.hhplus.concert.domain.model.concert.ConcertSeat;

import java.util.List;

public interface ConcertSeatRepositoryCustom {
    public List<ConcertSeat> getPossibleSeats(long concertId , String concertDy);
}
