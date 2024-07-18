package com.hhplus.concert.domain.model.queue;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Setter
@Table(name="queue")
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private long queueId ;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "token", unique = true)
    private String token;
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "active_at")
    private Date activeAt;
    @Column(name = "expired_at")
    private Date expiredAt;

    public Queue(String userId, String token, String state, Date createdAt, Date activeAt, Date expiredAt) {
        this.userId = userId;
        this.token = token;
        this.state = state;
        this.createdAt = createdAt;
        this.activeAt = activeAt;
        this.expiredAt = expiredAt;
    }

    public Queue(String userId, String token, String state , Date createdAt) {
        this.userId = userId;
        this.token = token;
        this.state = state;
        this.createdAt = createdAt;
    }

    public Queue() {}

    public long getQueueId() {
        return queueId;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getState() {
        return state;
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
}
