package com.logging.impl;

import com.google.common.base.Joiner;
import com.logging.Event;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CacheFileEventLogger extends FileEventLogger {

    private int cacheSize;
    private List<Event> cache;

    public CacheFileEventLogger(File file, int cacheSize) {
        super(file);
        this.cacheSize = cacheSize;
        cache = new ArrayList<>(cacheSize);
    }

    @Override
    public void logEvent(Event event) {
        cache.add(event);

        if (cacheSize == cache.size()) {
            super.logEvent(Joiner.on("\n").join(cache));
            cache.clear();
        }
    }

    private void destroy() {
        if (!cache.isEmpty()) {
            super.logEvent(Joiner.on("\n").join(cache));
        }
    }
}
