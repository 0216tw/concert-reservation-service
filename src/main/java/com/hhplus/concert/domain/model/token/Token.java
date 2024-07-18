package com.hhplus.concert.domain.model.token;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Entity
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private long queueId;
    @Column(name = "user_id")
    private String userId; //uuid 가 들어감
    private String token;
    private String status;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "active_at")
    private Date activeAt;
    @Column(name = "expired_at")
    private Date expiredAt;

    public long getQueueId() {
        return queueId;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getActiveAt() {
        return activeAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public Token(String userId, String token, String status , Date createdAt) {
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Token() {}
}
