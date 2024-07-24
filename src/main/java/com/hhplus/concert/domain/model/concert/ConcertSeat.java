package com.hhplus.concert.domain.model.concert;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Table(name="concert_seat")
public class ConcertSeat { //복합키 구성 (concert_id , concert_dy , seat_no )

    @EmbeddedId
    private ConcertSeatId id;

    @Column(name = "seat_grade")
    private String seatGrade;
    private long price;
    private String state;

    @Version
    private int version;

    public ConcertSeat(ConcertSeatId id, String seatGrade, long price, String state) {
        this.id = id;
        this.seatGrade = seatGrade;
        this.price = price;
        this.state = state;
    }

    public ConcertSeat(){}

    public ConcertSeatId getId() {
        return id;
    }

    public String getSeatGrade() {
        return seatGrade;
    }

    public long getPrice() {
        return price;
    }

    public String getState() {
        return state;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

