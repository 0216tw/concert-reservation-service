package com.hhplus.concert.adapter.repository.concert;

import com.hhplus.concert.domain.model.concert.Concert;

import java.util.List;

public interface ConcertRepositoryCustom {

    public List<Concert> getPossibleConcerts();

}
