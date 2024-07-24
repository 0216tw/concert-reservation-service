package com.hhplus.concert.adapter.repository.token;

import com.hhplus.concert.domain.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token , Long> {

}
