package com.hhplus.concert.unit.service.token;


import com.hhplus.concert.domain.service.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenServiceBase {

    @Autowired
    TokenService tokenService;

    @BeforeEach
    public void setUp() {
        tokenService = new TokenService();
    }


}