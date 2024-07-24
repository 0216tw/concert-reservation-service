package com.hhplus.concert.adapter.repository.user;

public interface UserRepositoryCustom {
    public long charge(String userId , long charge);
    public long getBalance(String userId) ;

    public long use(String userId , long charge);
}
