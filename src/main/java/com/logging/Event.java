package com.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;

@Component
@Scope(value = "prototype")
public class Event {

    private final int id = (int) (Math.random() * 100);
    @Resource(name = "newDate")
    private Date date;

    @Resource(name = "dateFormat")
    private DateFormat dateFormat;

    private String message;

    public Event() {

    }

    public Event(final Date date, final DateFormat dateFormatter) {

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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
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
