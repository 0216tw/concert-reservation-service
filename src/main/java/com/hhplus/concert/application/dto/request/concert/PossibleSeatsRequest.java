package com.hhplus.concert.application.dto.request.concert;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PossibleSeatsRequest {

    @NotNull(message = "concertId는 필수입력입니다.")
    private long concertId;
}
