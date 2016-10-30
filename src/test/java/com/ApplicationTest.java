package com;

import com.google.common.collect.ImmutableMap;
import com.logging.EventLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ApplicationTest {


    private Map<String, String> replacingValues;

    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Client client = new Client("25", "Bob");
        DummyEventLogger eventLogger = new DummyEventLogger();
        Application application = new Application(client, eventLogger);

        replacingValues = ImmutableMap.<String, String>builder()
                .put(client.getObjectId(), client.getName())
                .build();

        invokeLogEvent(application, "info about user = 25");
        Assert.assertTrue(eventLogger.getMsg().equals("info about user = Bob"));

        invokeLogEvent(application, "info about user = 0");
        Assert.assertTrue(eventLogger.getMsg().equals("info about user = 0"));

    }

    private void invokeLogEvent(Application app, String message) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = app.getClass().getDeclaredMethod("logEvent", String.class, Map.class);
        method.setAccessible(true);
        method.invoke(app, message, replacingValues);
        method.setAccessible(false);

    }
    private class DummyEventLogger implements EventLogger{

        private String msg;
        @Override
        public void logEvent(String message) {

            this.msg = message;
        }

        public String getMsg() {
            return msg;
        }
    }

}