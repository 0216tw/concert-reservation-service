package com.hhplus.concert.domain.service.token;

import com.hhplus.concert.common.constants.MessageEnum;
import com.hhplus.concert.common.exception.BusinessException;
import com.hhplus.concert.domain.model.token.Token;
import com.hhplus.concert.domain.service.reservation.ReservationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 600000; //액세스 토큰 60000밀리초

    public Token createToken() {
        log.info("[토큰 생성]");
        String userId = UUID.randomUUID().toString();
        Date createdAt = new Date(System.currentTimeMillis());
        String token = Jwts.builder()
                           .setSubject(userId)
                           .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                           .signWith(key)
                           .compact();
        String status = "WAIT";
        return new Token(userId , token , status , createdAt);
    }

    public String validateToken(String token) {
        log.info("[토큰 검증] token : {}" , token);

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            log.warn("[토큰 검증 오류] {}" , MessageEnum.TOKEN_VALIDATION_FAILURE.getMessage());
            throw new BusinessException("ERR" , MessageEnum.TOKEN_VALIDATION_FAILURE);
        }
    }



}
