package com;

import com.google.common.collect.ImmutableMap;
import com.logging.Event;
import com.logging.EventLogger;
import com.logging.EventType;
import com.sun.istack.internal.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;
import java.util.Map;

/**
 * The class using for demonstrating dependency injection
 */
public class Application implements Serializable {

    /**
     * Serialization Number
     */
    private static final long serialVersionUID = 1;
    /**
     * Bean of Client class
     *
     * @see Client
     */
    private final Client client;

    /**
     * Bean of EventLogger class
     *
     * @see EventLogger
     */
    private final Map<EventType, EventLogger> eventLoggers;
    private final EventLogger defaultLogger;

    /**
     * Constructor using for creation main program with all necessary beans
     *
     * @param client       Bean of Client class
     * @param eventLoggers Collections of beans with key = EventType and value = EventLogger class
     */
    public Application(Client client, Map<EventType, EventLogger> eventLoggers, EventLogger defaultEventLogger) {
        this.client = client;
        this.eventLoggers = eventLoggers;
        this.defaultLogger = defaultEventLogger;
    }

    /**
     * Execution point
     *
     * @param args execution params
     */
    public static void main(String[] args) {

        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        Application application = (Application) context.getBean("application");

        Map<String, String> replacingValues = ImmutableMap.<String, String>builder()
                .put(application.client.getObjectId(), application.client.getName())
                .build();
        String message = application.substituteMacroses("Info about 1 user", replacingValues);
        //application.logEvent(message);

        Event event = (Event) context.getBean("event");
        event.setMessage(message);
        application.logEvent(EventType.INFO, event);

        event.setMessage(application.substituteMacroses("Info about 0 user", replacingValues));
        application.logEvent(EventType.ERROR, event);

        event.setMessage(application.substituteMacroses("Info about 1 user", replacingValues));
        application.logEvent(null, event);

        context.close();

    }



    private String substituteMacroses(String message, @NotNull Map<String, String> replacingValues) {
        String logMessage = message;
        for (Map.Entry<String, String> pair : replacingValues.entrySet()) {
            logMessage = message.replaceAll(pair.getKey(), pair.getValue());
        }
        return logMessage;
    }

    private void logEvent(EventType eventType, Event event){

        EventLogger logger = eventLoggers.get(eventType);
        if (logger == null) {
            logger = defaultLogger;
        }

        logger.logEvent(event);
    }
}
