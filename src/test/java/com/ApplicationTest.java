package com;

import com.google.common.collect.ImmutableMap;
import com.logging.Event;
import com.logging.EventLogger;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

public class ApplicationTest {


    private Map<String, String> replacingValues;

    @Test
    public void testLogEventWithStringArg() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Client client = new Client("25", "Bob");
        DummyEventLogger eventLogger = new DummyEventLogger();
        Application application = new Application(client, eventLogger);

        replacingValues = ImmutableMap.<String, String>builder()
                .put(client.getObjectId(), client.getName())
                .build();
        String message =  "info about user = {0}";
        Map<Class<?>, Object> paramTypesValuesForSubstituteMacros = ImmutableMap.<Class<?>, Object>builder()
                .put(String.class, message)
                .put(Map.class, replacingValues)
                .build();
        Map<Class<?>, Object> paramTypesValuesForLogEvent = ImmutableMap.<Class<?>, Object>builder()
                .put(String.class, paramTypesValuesForSubstituteMacros)
                .build();

        invokeMethod(application, "logEvent", paramTypesValuesForLogEvent);
        Assert.assertTrue(eventLogger.getMsg().contains("info about user = Bob"));
        String correctValue = application.substituteMacroses(MessageFormat.format(message, "0"), replacingValues);
        paramTypesValuesForLogEvent = ImmutableMap.<Class<?>, Object>builder()
                .put(String.class, correctValue)
                .build();
        invokeMethod(application, "logEvent", paramTypesValuesForLogEvent);
        Assert.assertTrue(eventLogger.getMsg().equals("info about user = 0"));

    }

    @Test
    public void testLogEventWithEventArg() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Client client = new Client("25", "Bob");
        DummyEventLogger eventLogger = new DummyEventLogger();
        Date date = new Date();
        DateFormat dateInstance = DateFormat.getDateInstance();
        Application application = new Application(client, eventLogger);

        replacingValues = ImmutableMap.<String, String>builder()
                .put(client.getObjectId(), client.getName())
                .build();
        String message = "info about user = {0}";
        Event event = new Event(date, dateInstance);
        Map<Class<?>, Object> paramTypesValues = ImmutableMap.<Class<?>, Object>builder()
                .put(Event.class, event)
                .build();

        event.setMessage(application.substituteMacroses(MessageFormat.format(message, "25"), replacingValues));
        invokeMethod(application, "logEventWithEvent", paramTypesValues);
        Assert.assertTrue(eventLogger.getMsg().contains("info about user = Bob"));
        Assert.assertTrue(eventLogger.getMsg().contains("id"));
        Assert.assertTrue(eventLogger.getMsg().contains("date=" + dateInstance.format(date)));

        event = new Event(date, dateInstance);
        paramTypesValues = ImmutableMap.<Class<?>, Object>builder()
                .put(Event.class, event)
                .build();
        event.setMessage(application.substituteMacroses(MessageFormat.format(message, "0"), replacingValues));
        invokeMethod(application, "logEventWithEvent", paramTypesValues);
        Assert.assertTrue(eventLogger.getMsg().contains("info about user = 0"));
    }


    private<T> Object invokeMethod(T instance, String methodName, Map<Class<?>, Object> paramTypesValues) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<?>[] paramTypes = new Class<?>[0];
        paramTypes = paramTypesValues.keySet().toArray(paramTypes);
        Object[] paramValues = new Object[0];
        paramValues = paramTypesValues.values().toArray(paramValues);

        Method method = instance.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);

        return method.invoke(instance, paramValues);
    }
    private class DummyEventLogger implements EventLogger{

        private String msg;
        @Override
        public void logEvent(String message) {

            this.msg = message;
        }

        @Override
        public void logEvent(Event event) {

            this.msg = event.toString();
        }

        public String getMsg() {
            return msg;
        }
    }

}