package com.hhplus.concert.adapter.repository.concert;

import com.hhplus.concert.domain.model.concert.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertScheduleRepository extends JpaRepository<ConcertSchedule, Long> , ConcertScheduleRepositoryCustom {

}
