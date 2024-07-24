package com.hhplus.concert.domain.model.payment;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Table(name="payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private long paymentId;
    @Column(name = "reservation_id")
    private long reservationId;
    @Column(name = "payment_date")
    private Date paymentDate;
    @Column(name = "price")
    private long price;

    public long getPaymentId() {
        return paymentId;
    }

    public long getReservationId() {
        return reservationId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public Payment(long reservationId, Date paymentDate , long price) {
        this.reservationId = reservationId;
        this.paymentDate = paymentDate;
        this.price = price;
    }

    public Payment(){}
}
