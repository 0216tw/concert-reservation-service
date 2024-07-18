package com.hhplus.concert.domain.model.user;


import jakarta.persistence.*;


@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;
    private String token;
    private long balance; //잔액정보

    public User(String userId , String token , long balance) {
        this.userId = userId;
        this.token = token;
        this.balance = balance;
    }

    public User() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
