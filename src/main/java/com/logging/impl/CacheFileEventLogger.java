package com.logging.impl;

import com.google.common.base.Joiner;
import com.logging.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CacheFileEventLogger extends FileEventLogger {

    @Value("${cacheSize}")
    private int cacheSize;

    private List<Event> cache;

    private CacheFileEventLogger(File file, int cacheSize) {
        super(file);
        this.cacheSize = cacheSize;

    }

    private CacheFileEventLogger(){
        super();
    }

    @PostConstruct
    private void initCache(){

        cache = new ArrayList<>(cacheSize);
    }
    @Override
    public void logEvent(Event event) {
        cache.add(event);

        if (cacheSize == cache.size()) {
            super.logEvent(event.setMessage(Joiner.on("\n").join(cache)));
            cache.clear();
        }
    }

    @PreDestroy
    private void destroy() {

        if (!cache.isEmpty()) {
            for (Event event : cache) {
                super.logEvent(event);
            }
        }
    }
}
