package com.hhplus.concert.adapter.repository.queue;

public interface QueueRepositoryCustom {
    public long getWaitNo(String uuid) ;
    public String getTokenStatus(String uuid) ;

    public void changeWaitToActiveTokens(int activeCount);

    public void removeExpiredTokens();

    public long countActiveState();

    public void changeActiveToExpiredTokensByUserId(String userId);

    public void changeActiveToExpiredTokensByActiveAt(long time);
}
