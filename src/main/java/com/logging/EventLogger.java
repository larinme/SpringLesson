package com.logging;

import java.io.Serializable;

public interface EventLogger extends Serializable {

    void logEvent(String message);
    void logEvent(Event event);
}
