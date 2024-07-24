package com.hhplus.concert.adapter.repository.queue;

import com.hhplus.concert.domain.model.queue.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue , Long> , QueueRepositoryCustom{

    Optional<Queue> findByToken(String token);


}
