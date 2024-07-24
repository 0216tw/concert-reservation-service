package com.hhplus.concert.repositoryImpl.concert;

import com.hhplus.concert.adapter.repository.concert.ConcertScheduleRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> getPossibleDates(long concertId) {

        String jpql = """
                select a.id.concertDy
                  from ConcertSchedule a
                 where exists (
                                select 'x'
                                  from ConcertSeat e
                                 where e.id.concertId = a.id.concertId
                                   and e.id.concertDy = a.id.concertDy
                                   and e.state = 'AVAILABLE'
                 )
                 and a.id.concertId = :concertId
                """;
        return entityManager.createQuery(jpql , String.class)
                .setParameter("concertId" , concertId)
                .getResultList();
    }
}
