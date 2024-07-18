package com.hhplus.concert.application.usecase;

import com.hhplus.concert.domain.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenValidationUseCase {

    @Autowired
    TokenService tokenService;

    public String validate(String token) throws RuntimeException {
        return tokenService.validateToken(token);
    }
}
