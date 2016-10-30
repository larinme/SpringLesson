package com.logging.impl;

import com.logging.EventLogger;
import org.apache.log4j.Logger;

public class ConsoleEventLogger implements EventLogger{

    private static final long serialVersionUID = 1;
    private static final transient Logger LOG = Logger.getLogger(ConsoleEventLogger.class);

    public void logEvent(final String message) {

        LOG.info(message);
    }
}
