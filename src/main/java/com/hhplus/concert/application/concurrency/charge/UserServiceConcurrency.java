package com.hhplus.concert.application.concurrency.charge;

import com.hhplus.concert.adapter.repository.user.UserRepositoryConcurrency;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.redis.DistributedLock;
import com.hhplus.concert.domain.model.user.User;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleObjectStateException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service

public class UserServiceConcurrency {

    private static final Logger log = LoggerFactory.getLogger(UserServiceConcurrency.class);
    @Autowired
    UserRepositoryConcurrency userRepositoryConcurrency;

    @Autowired
    private RedissonClient redissonClient;

    @Transactional //낙관
    public long chargeOptimisticLock(String userId, long charge) {

        User user = findUserById(userId);
        long afterCharge = user.getBalance() + charge;
        user.setBalance(afterCharge);
        log.info("[사용자 잔액 충전] userId : {}, 충전할 금액 : {}", userId, charge);

        try {
            return userRepositoryConcurrency.save(user).getBalance(); // 엔티티 저장
        } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException optimisticException) {
            log.trace("낙관적 락 충돌 발생");
            throw new BusinessException("ERR", MessageEnum.CHARGE_FAILURE);
        }
    }

    @Transactional //비관
    public long chargePessimisticLock(String userId, long charge) {
        try {
            User user = userRepositoryConcurrency.findByIdForUpdate(userId); // 비관적 락을 사용하여 사용자 조회
            long afterCharge = user.getBalance() + charge;
            user.setBalance(afterCharge);
            log.info("[사용자 잔액 충전] userId : {}, 충전할 금액 : {}", userId, charge);

            userRepositoryConcurrency.save(user); // 엔티티 저장
            return afterCharge;

        } catch (Exception e) {
            log.error("Exception occurred for userId: {}", userId, e);
            throw new BusinessException("ERR", MessageEnum.CHARGE_FAILURE);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long chargeDistributedLock(String userId, long charge) {

        RLock lock = redissonClient.getLock("USER_LOCK_" + userId);
        boolean isLocked = false;
        int retry = 3;
        while (retry-- > 0) {
            try {
                if (lock.tryLock(120, 120, TimeUnit.SECONDS)) {
                    isLocked = true;
                    User user = findUserById(userId);
                    long afterCharge = user.getBalance() + charge;
                    user.setBalance(afterCharge);
                    userRepositoryConcurrency.save(user); // 엔티티 저장
                    return afterCharge;
                }
            } catch (StaleObjectStateException | OptimisticLockException e) {
                try {
                    log.warn("Locking conflict for userId: {}, retrying...", userId, e);
                    Thread.sleep(1000); // 잠시 대기 후 재시도
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                log.error("Exception occurred for userId: {}", userId, e);
                throw new BusinessException("ERR", MessageEnum.CHARGE_FAILURE);
            } finally {
                if (isLocked) {
                    lock.unlock();
                }
            }
        }
        throw new BusinessException("ERR", MessageEnum.CHARGE_FAILURE);
    }

    private User findUserById(String userId) {
        return userRepositoryConcurrency.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public long useBalance(String userId, long use) {
        User user = findUserById(userId); //사용자 검증
        long balance = user.getBalance();
        user.setBalance(balance + use);
        log.info("[사용자 잔액 사용] userId : {} , 기존금액 : { } , 사용할 금액 : {}", userId, balance, use);
        return userRepositoryConcurrency.save(user).getBalance();
    }


    public User insertUser(User user) {
        log.info("[사용자 등록]");
        return userRepositoryConcurrency.save(user);

    }


}
