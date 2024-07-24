package com.hhplus.concert.application.usecase;


import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.model.concert.Concert;
import com.hhplus.concert.domain.service.concert.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PossibleConcertsUseCase {
    @Autowired
    ConcertService concertService;

    public ApiResponse getPossibleConcerts() {
        return new ApiResponse("OK" , MessageEnum.SELECT_OK , concertService.getPossibleConcerts());
    }
}
