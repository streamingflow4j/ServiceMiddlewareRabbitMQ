package com.service.middleware.util;


import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.handler.annotation.Payload;

import com.service.middleware.cep.handler.MonitorEventHandler;
import com.service.middleware.model.Entity;
import com.service.middleware.model.ParserJson;

@MessageEndpoint
public class ModelTransformer  implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	MonitorEventHandler monitorEventHandler;
	

	public Entity toModel(@Payload String payload){
		Entity myEntity = ParserJson.parseEntity(payload);
		try {
			if(monitorEventHandler==null){
			  monitorEventHandler = (MonitorEventHandler)ApplicationContextProvider.getBean("monitorEventHandler");
			}
			monitorEventHandler.createRequestMonitorExpression(myEntity);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		
		return myEntity;
	}
}
