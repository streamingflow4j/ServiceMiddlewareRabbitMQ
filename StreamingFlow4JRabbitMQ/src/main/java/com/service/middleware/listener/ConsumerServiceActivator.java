package com.service.middleware.listener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import com.service.middleware.cep.handler.MonitorEventHandler;
import com.service.middleware.model.Entity;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ConsumerServiceActivator {

    @Autowired
	private MonitorEventHandler monitorEventHandler;
	
	private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "${queue.streaming.data}")
	public void listenQueueA(@Payload Message payload)  throws NumberFormatException,
			NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException{
		payload.getMessageProperties().setContentType("application/json");
		Entity event = objectMapper.readValue(payload.getBody(), Entity.class);
		monitorEventHandler.handleEntity(event);
        log.info("Message receive from MainQueue: {}", payload);
	}

}
