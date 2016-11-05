package com.spring;

import com.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.text.DateFormat;
import java.util.Date;

@Configuration
@PropertySource("classpath:properties/client.properties")
public class ApplicationConfig {

    @Autowired
    private Environment environment;

    @Bean
    public Date newDate() {
        return new Date();
    }

    @Bean
    public DateFormat dateFormat() {
        return DateFormat.getDateInstance();
    }

    @Bean
    public Client getClient() {

        Client client = new Client();
        client.setObjectId(environment.getRequiredProperty("id"));
        client.setName(environment.getRequiredProperty("name"));
        client.setGreetings(environment.getProperty("greeting"));
        return client;
    }
}
