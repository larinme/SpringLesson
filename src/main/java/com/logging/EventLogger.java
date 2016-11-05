package com.logging;

import java.io.Serializable;

public interface EventLogger extends Serializable {

    void logEvent(Event event);
}
