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
public class ConcertScheduleId implements Serializable {

    @Column(name = "concert_id")
    private long concertId;
    @Column(name = "concert_dy")
    private String concertDy;

    public long getConcertId() {
        return concertId;
    }

    public String getConcertDy() {
        return concertDy;
    }

    public ConcertScheduleId(long concertId, String concertDy) {
        this.concertId = concertId;
        this.concertDy = concertDy;
    }

    public ConcertScheduleId() {
    }
}
