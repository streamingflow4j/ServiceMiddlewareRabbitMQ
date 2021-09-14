package com.example.consumer.rabbitmq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQConsumer {
    
	private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);
	
	@RabbitListener(queues = "${rabbitmq.consumer.data.queue}")
	public void recievedMessage(String message) {
		log.info("Recieved Message From RabbitMQ: {}", message);
	}
}
