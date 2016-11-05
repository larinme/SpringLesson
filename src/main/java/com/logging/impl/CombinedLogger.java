package com.logging.impl;

import com.logging.Event;
import com.logging.EventLogger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Component
public class CombinedLogger implements EventLogger {

    @Resource(name = "combinedLoggers")
    private Collection<EventLogger> loggers;

    public CombinedLogger(List<EventLogger> loggers) {
        super();
        this.loggers = loggers;
    }

    public CombinedLogger() {
    }


    @Override
    public void logEvent(Event event) {

        for (EventLogger logger : loggers) {
            logger.logEvent(event);
        }
    }
}
