package com.hhplus.concert.repositoryImpl.reservation;

import com.hhplus.concert.adapter.repository.reservation.ReservationRepositoryCustom;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository

public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(ReservationRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public int updateSeatStatus(long concertId, String concertDy, long seatNo , String before , String after) {

        String jpql = """
                update ConcertSeat a
                   set a.state = :after
                 where a.id.concertId = :concertId
                   and a.id.concertDy = :concertDy
                   and a.id.seatNo = :seatNo
                   and a.state = :before
                """;
        try {
            return entityManager.createQuery(jpql)
                    .setParameter("concertId", concertId)
                    .setParameter("concertDy", concertDy)
                    .setParameter("seatNo", seatNo)
                    .setParameter("before", before)
                    .setParameter("after", after)
                    .executeUpdate();
        } catch(OptimisticEntityLockException e) {
            log.warn("[좌석 예약 동시성 오류] {} , concertId : {} , concertDy : {} , seatNo : {}" , MessageEnum.ALREADY_RESERVED , concertId , concertDy , seatNo);
            throw new BusinessException("ERR" , MessageEnum.ALREADY_RESERVED);
        }
    }

    @Override
    @Transactional
    @Modifying
    public int setNullTempReservationInfo(long reservationId) {
        String jpql = """
                update Reservation a
                   set a.tempReserveUserId = null 
                     , a.tempReserveExpiredAt = null
                 where a.reservationId = :reservationId
                """;

        return entityManager.createQuery(jpql)
                .setParameter("reservationId" , reservationId)
                .executeUpdate();
    }
}
