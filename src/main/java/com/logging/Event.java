package com.logging;

import java.text.DateFormat;
import java.util.Date;

public class Event {

    private final int id = (int) (Math.random() * 100);
    private final Date date;
    private final DateFormat dateFormat;
    private String message;

    public Event(final Date date, final DateFormat dateFormatter){

        this.date = date;
        this.dateFormat = dateFormatter;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Event setMessage(String message) {
        this.message = message;
        return this;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", date=" + dateFormat.format(date) +
                ", message='" + message + '\'' +
                '}';
    }
}
