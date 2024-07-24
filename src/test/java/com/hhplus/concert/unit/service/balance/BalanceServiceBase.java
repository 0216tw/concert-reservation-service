package com.hhplus.concert.unit.service.balance;

import com.hhplus.concert.adapter.repository.user.UserRepository;
import com.hhplus.concert.domain.model.user.User;
import com.hhplus.concert.domain.service.token.TokenService;
import com.hhplus.concert.domain.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BalanceServiceBase {

    @InjectMocks
    TokenService tokenService;


    @InjectMocks
    UserService balanceService;
    @Mock
    UserRepository balanceRepository;


    List<User> mockUser;

    @BeforeEach
    public void setUp() { //테스트용 데이터 셋업

        tokenService = new TokenService();
        balanceService = new UserService();
        mockUser = List.of( new User("111111111" , "token0token0token0" , 2000L ) ,
                new User("222222222" , "token1token1token1" , 5000L ) ,
                new User("333333333" , "token2token2token2" , 8000L )
                ) ;
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void close() { mockUser = null; }
}
