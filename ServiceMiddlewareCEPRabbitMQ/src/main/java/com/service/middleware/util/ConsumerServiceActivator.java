package com.service.middleware.util;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.handler.annotation.Payload;

import com.service.middleware.cep.handler.MonitorEventHandler;
import com.service.middleware.model.Entity;
import com.service.middleware.model.ParserJson;

@MessageEndpoint
public class ConsumerServiceActivator {
	
	@Autowired
	MonitorEventHandler monitorEventHandler;
	
    public void disparaEvento(@Payload String payload) throws NumberFormatException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    	monitorEventHandler = (MonitorEventHandler)ApplicationContextProvider.getBean("monitorEventHandler");    	
    	Entity event = ParserJson.parseEntity(payload);
    	monitorEventHandler.handleEntity(event);
    	
    }
    
}
