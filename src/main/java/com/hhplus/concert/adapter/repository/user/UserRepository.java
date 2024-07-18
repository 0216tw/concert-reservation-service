package com.hhplus.concert.adapter.repository.user;

import com.hhplus.concert.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> , UserRepositoryCustom {
}
