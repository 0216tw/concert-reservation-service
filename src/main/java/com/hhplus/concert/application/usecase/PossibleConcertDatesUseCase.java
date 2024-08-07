package com.hhplus.concert.application.usecase;


import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.domain.service.concert.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
* 유스케이스 : 비즈니스 로직을 캡슐화한다. (수행에 필요한 작업이나 기능을 정의)
*            도메인 모델로 비즈니스 로직을 구현하고, 외부 시스템과 상호작용을 조정한다.
*/
@Component
public class PossibleConcertDatesUseCase {

    @Autowired
    private ConcertService concertService;

    public ApiResponse getPossibleConcertDates(long concertId) {
        return new ApiResponse("OK" , MessageEnum.SELECT_OK , concertService.getPossibleDates(concertId));
    }
}
