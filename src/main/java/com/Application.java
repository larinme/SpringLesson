package com;

import com.google.common.collect.ImmutableMap;
import com.logging.Event;
import com.logging.EventLogger;
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
    private final EventLogger eventLogger;

    /**
     * Constructor using for creation main program with all necessary beans
     *
     * @param client       Bean of Client class
     * @param eventLogger Bean of EventLogger class
     */
    public Application(Client client, EventLogger eventLogger) {
        this.client = client;
        this.eventLogger = eventLogger;
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
        for (int i = 0; i < 24; i++) {

            application.logEventWithEvent(event);
        }


        context.close();

    }

    private void logEvent(String message) {
        eventLogger.logEvent(message);
    }

    public String substituteMacroses(String message, @NotNull Map<String, String> replacingValues) {
        String logMessage = message;
        for (Map.Entry<String, String> pair : replacingValues.entrySet()) {
            logMessage = message.replaceAll(pair.getKey(), pair.getValue());
        }
        return logMessage;
    }

    private void logEventWithEvent(Event event){

        eventLogger.logEvent(event);
    }
}
