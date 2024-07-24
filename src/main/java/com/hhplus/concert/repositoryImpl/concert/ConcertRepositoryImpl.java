package com.hhplus.concert.repositoryImpl.concert;

import com.hhplus.concert.adapter.repository.concert.ConcertRepositoryCustom;
import com.hhplus.concert.domain.model.concert.Concert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConcertRepositoryImpl implements ConcertRepositoryCustom  {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Concert> getPossibleConcerts() {

        String jpql = """
            select A
              from Concert A
             where exists ( 
                            select 1 
                              from ConcertSeat B   
                             WHERE A.concertId = B.id.concertId 
                           ) 
            """;

        return entityManager.createQuery(jpql , Concert.class)
                .getResultList();
    }




}