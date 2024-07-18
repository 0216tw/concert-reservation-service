package com.hhplus.concert.adapter.controller.concert;


import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.usecase.PossibleConcertDatesUseCase;
import com.hhplus.concert.application.usecase.PossibleConcertSeatsUseCase;
import com.hhplus.concert.application.usecase.PossibleConcertsUseCase;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcertController {

    private static final Logger log = LoggerFactory.getLogger(ConcertController.class);
    @Autowired
    PossibleConcertsUseCase possibleConcertsUseCase;
    @Autowired
    PossibleConcertDatesUseCase possibleConcertDatesUseCase;
    @Autowired
    PossibleConcertSeatsUseCase possibleConcertSeatsUseCase;


    @GetMapping("/api/concerts")
    public ResponseEntity<ApiResponse> findConcertAll() {
        log.info("[콘서트 목록 조회 컨트롤러 진입]");
        ApiResponse response = possibleConcertsUseCase.getPossibleConcerts();
        log.info("[콘서트 목록 조회 완료]");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/concerts/{concertId}/dates") //콘서트 예약 가능 날짜 조회
    public ResponseEntity<ApiResponse> findPossibleDates(@PathVariable("concertId") long concertId) {

        log.info("[콘서트 예약 가능 날짜 조회 컨트롤러 진입] concertId: {}", concertId);
        if(concertId <= 0) {
            log.warn("[Bad Request] 잘못된 파라미터 : concertId -> {}" , concertId);
            throw new BusinessException("ERR" , MessageEnum.BAD_REQUEST);
        }

        ApiResponse response =  possibleConcertDatesUseCase.getPossibleConcertDates(concertId);
        log.info("[콘서트 예약 가능 날짜 조회 완료]");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/concerts/{concertId}/dates/{concertDy}/seats") //콘서트 예약 가능 좌석 조회
    public ResponseEntity<ApiResponse> PossibleDatesResponse(@PathVariable("concertId") long concertId , @PathVariable("concertDy") String concertDy) {
        log.info("[콘서트 예약 가능 좌석 조회 컨트롤러 진입] concertId : {} , concertDy : {}" , concertId , concertDy);
        ApiResponse response = possibleConcertSeatsUseCase.getPossibleConcertSeats(concertId , concertDy);
        log.info("[콘서트 예약 가능 좌석 조회 완료]");
        return ResponseEntity.ok(response);

    }
}
