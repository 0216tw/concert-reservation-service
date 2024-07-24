package com.hhplus.concert.repositoryImpl.queue;

import com.hhplus.concert.adapter.repository.queue.QueueRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public class QueueRepositoryImpl implements QueueRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    public long getWaitNo(String userId) {
        String jpqlSelect = """
                select a.queueId -
                       (select min(b.queueId)
                          from Queue b 
                         where b.state = 'WAIT' 
                        ) 
                  from Queue a 
                 where a.userId = :userId 
                """;

        return entityManager.createQuery(jpqlSelect , Long.class)
                .setParameter("userId" , userId)
                .getSingleResult();
    }

    public String getTokenStatus(String userId) {
        String jpqlSelect = """
                select a.state
                  from Queue a 
                 where a.userId = :userId 
                """;

        return entityManager.createQuery(jpqlSelect , String.class)
                .setParameter("userId" , userId)
                .getSingleResult();
    }

    public long countActiveState() {
        String jpql = """
                select count(q)
                  from Queue q
                 where q.state = 'ACTIVE'
                """;
        return entityManager.createQuery(jpql , Long.class).getSingleResult();
    }

    @Transactional
    @Modifying
    public void changeWaitToActiveTokens(int activeCount) {

        //순서대로 WAIT 인 대상 추출
        String jpqlSelect = """
                            select q.queueId
                              from Queue q
                             where q.state = 'WAIT'
                             order by q.createdAt
                            """;
        List<Long> queueIds = entityManager.createQuery(jpqlSelect, Long.class)
                .setMaxResults(50 - activeCount)
                .getResultList();

        String jpqlUpdate = """
                update Queue q
                   set q.state = 'ACTIVE'
                     , q.activeAt = now()
                 where q.queueId in :queueIds
                """;
        entityManager.createQuery(jpqlUpdate)
                .setParameter("queueIds" , queueIds)
                .executeUpdate();
    }

    @Override
    @Transactional
    @Modifying
    public void removeExpiredTokens() {
        String jpql = """
                delete from Queue a
                 where state = 'EXPIRED'
                """;

        entityManager.createQuery(jpql).executeUpdate();
    }

    @Override
    @Transactional
    @Modifying
    public void changeActiveToExpiredTokensByUserId(String userId) {

        LocalDateTime expirationTime = LocalDateTime.now();

         String jpql = """
                 update Queue a
                    set a.state = 'EXPIRED'
                      , a.expiredAt = :expirationTime
                  where a.queueId = ( select max(a.queueId) from Queue a where userId = :userId )
                 """;
        entityManager.createQuery(jpql).setParameter("userId" , userId).setParameter("expirationTime", expirationTime).executeUpdate();

    }

    @Override
    @Transactional
    @Modifying
    public void changeActiveToExpiredTokensByActiveAt(long seconds) {

        LocalDateTime expirationTime = LocalDateTime.now().minus(seconds, ChronoUnit.SECONDS);

        String jpql = """
                    update Queue a 
                    set a.state = 'EXPIRED'
                    where a.activeAt < :expirationTime
                """;

        entityManager.createQuery(jpql)
                .setParameter("expirationTime", expirationTime)
                .executeUpdate();
    }
}


