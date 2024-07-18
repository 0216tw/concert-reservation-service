package com.hhplus.concert.adapter.repository.reservation;

import com.hhplus.concert.domain.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> , ReservationRepositoryCustom{

}
