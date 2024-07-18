package com.hhplus.concert.domain.model.concert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
public class ConcertSeatId implements Serializable {
    @Column(name = "concert_id")
    private long concertId;
    @Column(name = "concert_dy")
    private String concertDy;
    @Column(name = "seat_no")
    private long seatNo;

    public ConcertSeatId(long concertId, String concertDy, long seatNo) {
        this.concertId = concertId;
        this.concertDy = concertDy;
        this.seatNo = seatNo;
    }

    public long getConcertId() {
        return concertId;
    }

    public String getConcertDy() {
        return concertDy;
    }

    public long getSeatNo() {
        return seatNo;
    }

    public ConcertSeatId() {
    }
}
