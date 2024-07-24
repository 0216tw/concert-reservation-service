package com.hhplus.concert.adapter.repository.reservation;

public interface ReservationRepositoryCustom {

    public int updateSeatStatus(long concertId, String concertDy, long seatNo, String before, String after);

    public int setNullTempReservationInfo(long reservationId);
}


