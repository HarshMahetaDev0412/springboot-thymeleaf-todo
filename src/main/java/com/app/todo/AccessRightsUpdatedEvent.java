package com.app.todo;

import org.springframework.context.ApplicationEvent;

public class AccessRightsUpdatedEvent extends ApplicationEvent {
    public AccessRightsUpdatedEvent(Object source) {
    	
        super(source);
        System.out.println("AccessRightsUpdatedEvent : Event Injected!!");
    }
}

