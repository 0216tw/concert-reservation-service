package com.hhplus.concert.adapter.controller.token;
import com.hhplus.concert.application.dto.response.ApiResponse;
import com.hhplus.concert.application.dto.response.token.TokenCreationResponse;
import com.hhplus.concert.application.usecase.TokenCreationUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TokenController {
    private static final Logger log = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    TokenCreationUseCase createTokenUseCase;

    @PostMapping("/api/token")
    public ResponseEntity<ApiResponse> createToken() {
        log.info("[토큰 생성 요청 컨트롤러 진입]");
        ApiResponse response = createTokenUseCase.createToken();
        log.info("[토큰 생성 완료]");
        return ResponseEntity.ok(response);
    }
}
