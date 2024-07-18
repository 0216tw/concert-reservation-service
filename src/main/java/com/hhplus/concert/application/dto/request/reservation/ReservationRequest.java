package com.hhplus.concert.application.dto.request.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class ReservationRequest {

    @NotNull(message = "concertId는 필수입력입니다.")
    private long concertId;
    @NotNull(message = "concertDy는 필수입력입니다.")
    @NotBlank(message = "concertDy는 필수입력입니다.")
    private String concertDy;
    @NotNull(message = "seatNo는 필수입력입니다.")
    private long seatNo;

    public ReservationRequest(long concertId, String concertDy, long seatNo) {
        this.concertId = concertId;
        this.concertDy = concertDy;
        this.seatNo = seatNo;
    }

    public ReservationRequest() {
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
}
