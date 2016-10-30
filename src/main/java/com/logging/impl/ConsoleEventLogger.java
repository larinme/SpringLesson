package com.logging.impl;

import com.logging.EventLogger;
import org.apache.log4j.Logger;

public class ConsoleEventLogger implements EventLogger{

    private Logger log;

    public void logEvent(String message) {

        log.info(message);
    }

    public ConsoleEventLogger() {

        log = Logger.getLogger(ConsoleEventLogger.class);
    }
}
