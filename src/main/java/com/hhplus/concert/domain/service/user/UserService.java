package com.hhplus.concert.domain.service.user;

import com.hhplus.concert.adapter.repository.user.UserRepository;
import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepository userRepository;

    public long charge(String userId , long charge) {
        findUserById(userId); //사용자 검증
        log.info("[사용자 잔액 충전] userId : {} , 충전할 금액 : {}" , userId , charge);
        return userRepository.charge(userId , charge);
    }

    public long use(String userId , long charge) {
        findUserById(userId); //사용자 검증
        log.info("[사용자 잔액 사용] userId : {} , 사용할 금액 : {}" , userId , charge);
        return userRepository.use(userId , charge);
    }

    public long getBalance(String userId) {
        findUserById(userId); //사용자 검증
        log.info("[사용자 잔액 조회] userId : {}" , userId);
        return userRepository.getBalance(userId);
    }

    public User insertUser(User user) {
        log.info("[사용자 등록]");
        return userRepository.save(user);

    }

    public User findUserById(String userId) {
        log.info("[사용자 검증] userId : {}" , userId);
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isEmpty()) {
            log.warn("[사용자 검증 오류] {}" ,MessageEnum.USER_NOT_FOUND.getMessage());
            throw new BusinessException("ERR" , MessageEnum.USER_NOT_FOUND);
        }
        return foundUser.get();
    }

}
