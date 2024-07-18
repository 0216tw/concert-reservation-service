package com.hhplus.concert.repositoryImpl.user;

import com.hhplus.concert.adapter.repository.user.UserRepositoryCustom;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long getBalance(String userId) {
        String jpqlSelect = """
                select a.balance
                  from User a
                 where a.userId = :userId
                """;

        return entityManager.createQuery(jpqlSelect , Long.class)
                .setParameter("userId" , userId)
                .getSingleResult();
    }

    @Override
    @Transactional
    public long charge(String userId, long charge) {

        String jpqlUpdate = """
                update User a
                   set a.balance = a.balance + :charge
                 where a.userId = :userId
                """;

        entityManager.createQuery(jpqlUpdate)
                .setParameter("charge", charge)
                .setParameter("userId", userId)
                .executeUpdate();

        String jpqlSelect = """
                select a.balance
                  from User a
                 where a.userId = :userId 
                """;
        try {
            return entityManager.createQuery(jpqlSelect, Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();

        } catch (EmptyResultDataAccessException e) {
            log.warn("[사용자 검증 오류] {} , userId : {}", MessageEnum.USER_NOT_FOUND.getMessage(), userId);
            throw new BusinessException("ERR", MessageEnum.USER_NOT_FOUND);
        }

    }

    @Override
    @Transactional
    public long use(String userId, long charge) {

        String jpqlUpdate = """
                update User a
                   set a.balance = a.balance - :charge
                 where a.userId = :userId
                """;

        entityManager.createQuery(jpqlUpdate)
                .setParameter("charge" , charge)
                .setParameter("userId" , userId)
                .executeUpdate();

        String jpqlSelect = """
                select a.balance 
                  from User a 
                 where a.userId = :userId 
                """;

        try {
            return entityManager.createQuery(jpqlSelect , Long.class)
                    .setParameter("userId" , userId)
                    .getSingleResult();
        } catch(EmptyResultDataAccessException e) {
            log.warn("[사용자 검증 오류] {} , userId : {}", MessageEnum.USER_NOT_FOUND.getMessage(), userId);
            throw new BusinessException("ERR" , MessageEnum.USER_NOT_FOUND);
        }
    }
}

