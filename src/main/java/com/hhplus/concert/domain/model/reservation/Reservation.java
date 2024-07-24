package com.hhplus.concert.domain.model.reservation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private long reservationId;
    @Column(name = "concert_id")
    private long concertId;
    @Column(name = "concert_dy")
    private String concertDy;
    @Column(name = "seat_no")
    private long seatNo;
    @Column(name = "seat_grade")
    private String seatGrade;
    private long price;
    @Column(name = "temp_reserve_user_id")
    private String tempReserveUserId;
    @Column(name = "temp_reserve_expired_at")
    private Date tempReserveExpiredAt;

    public long getReservationId() {
        return reservationId;
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

    public String getSeatGrade() {
        return seatGrade;
    }

    public long getPrice() {
        return price;
    }

    public String getTempReserveUserId() {
        return tempReserveUserId;
    }

    public Date getTempReserveExpiredAt() {
        return tempReserveExpiredAt;
    }

    public Reservation(long concertId, String concertDy, long seatNo, String seatGrade, long price, String tempReserveUserId, Date tempReserveExpiredAt) {
        this.concertId = concertId;
        this.concertDy = concertDy;
        this.seatNo = seatNo;
        this.seatGrade = seatGrade;
        this.price = price;
        this.tempReserveUserId = tempReserveUserId;
        this.tempReserveExpiredAt = tempReserveExpiredAt;
    }

    public Reservation(long concertId, String concertDy, long seatNo , String userId) {
        this.concertId = concertId;
        this.concertDy = concertDy;
        this.seatNo = seatNo;
        this.tempReserveUserId = userId;
    }

    public Reservation() {}
}
