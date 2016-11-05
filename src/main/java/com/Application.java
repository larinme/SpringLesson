package com;

import com.logging.Event;
import com.logging.EventLogger;
import com.logging.EventType;
import com.spring.ApplicationConfig;
import com.spring.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

/**
 * The class using for demonstrating dependency injection
 */
@Service
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
    @Autowired
    private Client client;

    /**
     * Bean of EventLogger class
     *
     * @see EventLogger
     */
    @Resource(name = "loggerMap")
    private Map<EventType, EventLogger> eventLoggers;
    @Resource(name = "defaultLogger")
    private EventLogger defaultLogger;

    /**
     * Constructor using for creation main program with all necessary beans
     *
     * @param client       Bean of Client class
     * @param eventLoggers Collections of beans with key = EventType and value = EventLogger class
     */
    public Application(
            Client client,
            Map<EventType, EventLogger> eventLoggers,
            EventLogger defaultEventLogger) {
        this.client = client;
        this.eventLoggers = eventLoggers;
        this.defaultLogger = defaultEventLogger;
    }

    public Application() {
    }

    /**
     * Execution point
     *
     * @param args execution params
     */
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ApplicationConfig.class, LoggerConfig.class);
        ctx.scan("com");
        ctx.refresh();

        Application app = (Application) ctx.getBean("application");

        Client client = ctx.getBean(Client.class);
        System.out.println("Client says: " + client.getGreetings());

        Event event = ctx.getBean(Event.class);
        event.setMessage("Some event for 1");
        app.logEvent(EventType.INFO, event);

        event = ctx.getBean(Event.class);
        event.setMessage("Some event for 2");

        app.logEvent(EventType.ERROR, event);

        event = ctx.getBean(Event.class);
        event.setMessage("Some event for 3");

        app.logEvent(null, event);

        ctx.close();
    }


    private void logEvent(EventType eventType, Event event) {

        EventLogger logger = eventLoggers.get(eventType);
        if (logger == null) {
            logger = defaultLogger;
        }

        String message = event.getMessage();
        event.setMessage(message.replaceAll(client.getObjectId(), client.getName()));
        logger.logEvent(event);
    }
}
