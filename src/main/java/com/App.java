package com;

import com.google.common.collect.ImmutableMap;
import com.logging.EventLogger;
import com.logging.impl.ConsoleEventLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigInteger;
import java.util.Map;

public class App {

    private Client client;
    private EventLogger event_logger;

    public App(Client client, EventLogger event_logger){
        this.client = client;
        this.event_logger = event_logger;
    }

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        App app = (App) applicationContext.getBean("app");

        Map<String, String > replacingValues = ImmutableMap.<String, String>builder()
                .put(app.client.getId(), app.client.getName())
                .build();
        app.logEvent("Info about 1 user", replacingValues);

    }

    private void logEvent(String message, Map<String, String> replacingValues){
        for (Map.Entry<String, String> pair : replacingValues.entrySet()) {
            message = message.replaceAll(pair.getKey(), pair.getValue());
        }
        logEventWithoutMacros(message);
    }

    private void logEventWithoutMacros(String message) {
        event_logger.logEvent(message);
    }
}
