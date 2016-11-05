package com.logging.impl;

import com.logging.Event;
import com.logging.EventLogger;
import org.springframework.stereotype.Component;

@Component("consoleEventLogger")
public class ConsoleEventLogger implements EventLogger{

    private static final long serialVersionUID = 1;


    @Override
    public void logEvent(Event event) {
        System.out.println(event);
    }
}
