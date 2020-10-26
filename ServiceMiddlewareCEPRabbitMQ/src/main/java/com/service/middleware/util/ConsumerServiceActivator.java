package com.service.middleware.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.handler.annotation.Payload;

import com.service.middleware.cep.handler.MonitorEventHandler;
import com.service.middleware.model.Entity;

@MessageEndpoint
public class ConsumerServiceActivator {
	
	@Autowired
	MonitorEventHandler monitorEventHandler;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
    public void disparaEvento(@Payload String payload) throws NumberFormatException, 
    NoSuchMethodException, SecurityException, IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException, JsonParseException, JsonMappingException, IOException{
    	
    	monitorEventHandler = (MonitorEventHandler)ApplicationContextProvider.getBean("monitorEventHandler");    	
    	Entity event = objectMapper.readValue(payload, Entity.class); 
    	monitorEventHandler.handleEntity(event);
    	
    }
    
}
