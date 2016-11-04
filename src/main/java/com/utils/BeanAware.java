package com.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class BeanAware implements ApplicationContextAware, BeanNameAware,
        ApplicationEventPublisherAware {


    private ApplicationEventPublisher eventPublisher;
    private String name;
    private ApplicationContext ctx;
    @Override
    public void setBeanName(String beanName) {

        this.name = beanName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.ctx = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

        this.eventPublisher = applicationEventPublisher;
    }

    private void init() {
        System.out.println(this.getClass().getSimpleName() + " > My name is '"
                + name + "'");
        if (ctx != null) {
            System.out.println(this.getClass().getSimpleName()
                    + " > My context is " + ctx.getClass().toString());
        } else {
            System.out.println(this.getClass().getSimpleName()
                    + " > Context is not set");
        }
        if (eventPublisher != null) {
            System.out.println(this.getClass().getSimpleName()
                    + " > My eventPublisher is "
                    + eventPublisher.getClass().toString());
        } else {
            System.out.println(this.getClass().getSimpleName()
                    + " > EventPublisher is not set");
        }
    }
}
