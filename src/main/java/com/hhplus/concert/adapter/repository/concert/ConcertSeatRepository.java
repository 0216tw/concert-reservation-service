package com.hhplus.concert.adapter.repository.concert;

import com.hhplus.concert.domain.model.concert.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, Long> , ConcertSeatRepositoryCustom {

}
