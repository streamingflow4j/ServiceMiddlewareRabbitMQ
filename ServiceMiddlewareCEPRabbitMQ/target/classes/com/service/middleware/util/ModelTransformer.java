package com.service.middleware.util;


import java.io.IOException;
import java.io.Serializable;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.handler.annotation.Payload;

import com.service.middleware.cep.handler.MonitorEventHandler;
import com.service.middleware.model.Entity;

@MessageEndpoint
public class ModelTransformer  implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(ModelTransformer.class);
	
	@Autowired
	MonitorEventHandler monitorEventHandler;
	
	ObjectMapper objectMapper = new ObjectMapper();

	public Entity toModel(@Payload String payload) throws JsonParseException, JsonMappingException, IOException{

		Entity myEntity = objectMapper.readValue(payload, Entity.class); 
		try {
			if(monitorEventHandler == null){
			  monitorEventHandler = (MonitorEventHandler)ApplicationContextProvider.getBean("monitorEventHandler");
			}
			monitorEventHandler.createRequestMonitorExpression(myEntity);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Não é permitido inserir Entidade! ====>>> "+ e.getMessage());
		}
		
		return myEntity;
	}
}
