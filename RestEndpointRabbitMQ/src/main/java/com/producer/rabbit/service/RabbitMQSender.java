package com.producer.rabbit.service;



import java.util.HashMap;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.producer.rabbit.model.Entity;

@Service
public class RabbitMQSender {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	private static final String EXCHANE = "amq.direct";
	
	private static final String QUEUE_DATA = "si.queue.teste";
	
	private static final String QUEUE_RULE = "si.ceprule.queue";

	private ObjectMapper objectMapper = new ObjectMapper();
	
	private String payload = "";
	
	public void sendData(Entity entity) throws JsonProcessingException {
		this.send(entity,QUEUE_DATA); 
	}	
	
    public void sendRule(Entity entity) throws JsonProcessingException {
    	this.send(entity,QUEUE_RULE);    
	}
	
	
	public void send(Entity entity, String queue) throws JsonProcessingException{
		
		payload = objectMapper.writeValueAsString(entity);
		AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);
		amqpAdmin.declareQueue(new Queue(queue, true));
		amqpAdmin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, EXCHANE, queue, new HashMap<>()));		
	
		rabbitTemplate.setMessageConverter(new SimpleMessageConverter());		
		rabbitTemplate.convertAndSend(EXCHANE, queue, payload);
		System.out.println("Send msg = " + entity.toString());
	    
	}
	
	
}