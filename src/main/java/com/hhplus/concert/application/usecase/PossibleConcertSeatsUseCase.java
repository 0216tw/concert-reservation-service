package com.hhplus.concert.application.usecase;

import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.service.concert.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PossibleConcertSeatsUseCase {
    @Autowired
    private ConcertService concertService;

    public ApiResponse getPossibleConcertSeats(long concertId , String concertDy) {
        return new ApiResponse("OK" , MessageEnum.SELECT_OK , concertService.getPossibleSeats(concertId , concertDy));
    }
}
