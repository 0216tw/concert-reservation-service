package com.hhplus.concert.unit.service.concert;

import com.hhplus.concert.adapter.repository.concert.ConcertRepository;
import com.hhplus.concert.adapter.repository.concert.ConcertScheduleRepository;
import com.hhplus.concert.adapter.repository.concert.ConcertSeatRepository;
import com.hhplus.concert.domain.service.concert.ConcertService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ConcertServiceBase {

    @InjectMocks
    public ConcertService concertService;

    @Mock
    public ConcertScheduleRepository concertScheduleRepository;

    @Mock
    public ConcertSeatRepository concertSeatRepository;

    @Mock
    public ConcertRepository concertRepository;

    @BeforeEach
    public void setUp() {
        concertService = new ConcertService();
        MockitoAnnotations.openMocks(this);
    }

}
