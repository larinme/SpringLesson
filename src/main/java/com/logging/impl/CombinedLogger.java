package com.logging.impl;

import com.logging.Event;
import com.logging.EventLogger;

import java.util.Collection;

public class CombinedLogger implements EventLogger {

    private Collection<EventLogger> loggers;

    public CombinedLogger(Collection<EventLogger> loggers){
        super();
        this.loggers = loggers;
    }

    @Override
    public void logEvent(String message) {

    }

    @Override
    public void logEvent(Event event) {

        for (EventLogger logger : loggers) {
            logger.logEvent(event);
        }
    }
}
