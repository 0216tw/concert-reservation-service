package com.hhplus.concert.domain.model.concert;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Table(name="concert_schedule")
public class ConcertSchedule { //복합키 구성 (concert_id , concert_dy)

    @EmbeddedId
    private ConcertScheduleId id;

    private String location;
    @Column(name = "start_date")
    private Date startDate;

    public ConcertScheduleId getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public ConcertSchedule(ConcertScheduleId id, String location, Date startDate) {
        this.id = id;
        this.location = location;
        this.startDate = startDate;
    }

    public ConcertSchedule() {}
}

