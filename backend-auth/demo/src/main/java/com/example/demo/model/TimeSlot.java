package com.example.demo.model;  // Make sure this matches your project structure

import java.sql.Date;
import java.sql.Time;

public class TimeSlot {
    private Date date;
    private Time time;

    // Constructor
    public TimeSlot(Date date, Time time) {
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
