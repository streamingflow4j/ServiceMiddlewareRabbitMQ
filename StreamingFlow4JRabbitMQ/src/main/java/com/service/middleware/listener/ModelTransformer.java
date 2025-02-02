package com.service.middleware.listener;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.service.middleware.cep.handler.MonitorEventHandler;
import com.service.middleware.model.Entity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ModelTransformer {

	@Autowired
	private MonitorEventHandler monitorEventHandler;
	ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "si.ceprule.queue")
	public void toModel(@Payload Message payload) throws Exception {
		Entity myEntity = objectMapper.readValue(payload.getBody(), Entity.class);
		monitorEventHandler.createRequestMonitorExpression(myEntity);
	}

}
