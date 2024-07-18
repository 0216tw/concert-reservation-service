package com.hhplus.concert.domain.model.concert;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Table(name="concert")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private long concertId;
    @Column(name = "concert_name")
    private String concertName;
    private long age;
    @Column(name = "created_at")
    private Date createdAt;

    public Concert(long concertId, String concertName, long age, Date createdAt) {
        this.concertId = concertId;
        this.concertName = concertName;
        this.age = age;
        this.createdAt = createdAt;
    }

    public Concert() {}

    public long getConcertId() {
        return concertId;
    }

    public String getConcertName() {
        return concertName;
    }

    public long getAge() {
        return age;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
