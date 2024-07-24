package com.hhplus.concert.unit.service.reservation;


import com.hhplus.concert.adapter.repository.reservation.ReservationRepository;
import com.hhplus.concert.domain.service.reservation.ReservationService;
import com.hhplus.concert.domain.service.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class ReservationServiceBase {


    @InjectMocks
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;


    @BeforeEach
    public void setUp() {

        reservationService = new ReservationService();
        MockitoAnnotations.openMocks(this);
    }



}
