package com.hhplus.concert.application.dto.request.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
public class PaymentRequest {

    @NotNull(message = "reservationId는 필수입력입니다.")
    @Min(value = 1 , message = "reservationId는 필수입력입니다.")
    private long reservationId;

    public long getReservationId() {
        return reservationId;
    }

    public PaymentRequest(long reservationId) {
        this.reservationId = reservationId;
    }

    public PaymentRequest() {
    }
}
