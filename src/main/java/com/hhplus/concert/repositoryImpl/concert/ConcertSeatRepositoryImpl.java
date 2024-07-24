package com.hhplus.concert.repositoryImpl.concert;

import com.hhplus.concert.adapter.repository.concert.ConcertSeatRepositoryCustom;
import com.hhplus.concert.domain.model.concert.ConcertSeat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class ConcertSeatRepositoryImpl implements ConcertSeatRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ConcertSeat> getPossibleSeats(long concertId , String concertDy) {

        String jpql = """
                select a
                  from ConcertSeat a
                 where a.id.concertId = :concertId
                   and a.id.concertDy = :concertDy
                   and a.state = 'AVAILABLE'
                """;

        return entityManager.createQuery(jpql , ConcertSeat.class)
                .setParameter("concertId" , concertId)
                .setParameter("concertDy" , concertDy)
                .getResultList();
    }
}
