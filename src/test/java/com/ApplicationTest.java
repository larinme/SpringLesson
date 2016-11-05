package com;

import com.google.common.collect.ImmutableMap;
import com.logging.Event;
import com.logging.EventLogger;
import com.logging.EventType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class ApplicationTest {


    private static final Map<EventType, EventLogger> EVENT_LOGGERS = Collections.emptyMap();
    private static final String INFO_ABOUT_USER = "info about user = {0}";
    private Map<String, String> replacingValues;
    private DummyEventLogger eventLogger;
    private Date date;
    private DateFormat dateFormat;
    private Application application;
    private Event event;

    @Before
    public void initialize() {
        Client client = new Client("25", "Bob");
        eventLogger = new DummyEventLogger();
        date = new Date();
        dateFormat = DateFormat.getDateInstance();
        application = new Application(client, EVENT_LOGGERS, eventLogger);
        replacingValues = ImmutableMap.<String, String>builder()
                .put(client.getObjectId(), client.getName())
                .build();
        event = new Event(date, dateFormat);

    }

    @Test
    public void testNullTypeLogging() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        executeTestingMethods(EventType.NULL, 25);
        Assert.assertTrue(eventLogger.getMsg().contains("info about user = Bob"));
    }

    @Test
    public void testErrorTypeLogging() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        executeTestingMethods(EventType.ERROR, 0);
        Assert.assertTrue(eventLogger.getMsg().contains("info about user = 0"));
    }

    @Test
    public void testInfoTypeLogging() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        executeTestingMethods(EventType.INFO, 25);
        Assert.assertTrue(eventLogger.getMsg().contains("info about user = Bob"));
        Assert.assertTrue(eventLogger.getMsg().contains("id"));
        Assert.assertTrue(eventLogger.getMsg().contains("date=" + dateFormat.format(date)));
    }

    private void executeTestingMethods(EventType eventType, int testId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<Class<?>, Object> paramTypesValuesForSubstituteMacros = getParamsTypesValuesForSubstituteMacros(testId);
        Map<Class<?>, Object> paramTypesValues = getParamTypesValuesForLogEvent(eventType);

        event.setMessage((String) invokeMethod(application, "substituteMacroses", paramTypesValuesForSubstituteMacros));
        invokeMethod(application, "logEvent", paramTypesValues);
    }

    private Map<Class<?>, Object> getParamTypesValuesForLogEvent(EventType eventType) {
        return ImmutableMap.<Class<?>, Object>builder()
                .put(EventType.class, eventType)
                .put(Event.class, event)
                .build();
    }

    private Map<Class<?>, Object> getParamsTypesValuesForSubstituteMacros(int id) {
        return ImmutableMap.<Class<?>, Object>builder()
                .put(String.class, MessageFormat.format(INFO_ABOUT_USER, id))
                .put(Map.class, replacingValues)
                .build();
    }


    private <T> Object invokeMethod(T instance, String methodName, Map<Class<?>, Object> paramTypesValues)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<?>[] paramTypes = new Class<?>[0];
        paramTypes = paramTypesValues.keySet().toArray(paramTypes);
        Object[] paramValues = new Object[0];
        paramValues = paramTypesValues.values().toArray(paramValues);

        Method method = instance.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);

        try {
            return method.invoke(instance, paramValues);
        } finally {
            method.setAccessible(false);
        }
    }

    private class DummyEventLogger implements EventLogger {

        private String msg;



        @Override
        public void logEvent(Event event) {

            this.msg = event.toString();
        }

        private String getMsg() {
            return msg;
        }
    }

}