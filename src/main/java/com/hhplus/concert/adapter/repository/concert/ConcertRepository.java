package com.hhplus.concert.adapter.repository.concert;

import com.hhplus.concert.domain.model.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert , Long> , ConcertRepositoryCustom{

}
