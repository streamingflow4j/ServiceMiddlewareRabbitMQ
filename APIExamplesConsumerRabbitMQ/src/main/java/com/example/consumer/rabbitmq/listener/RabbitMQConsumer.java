package com.example.consumer.rabbitmq.listener;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQConsumer {
    
	private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);
	
	private static Queue<String> str_queue = new LinkedList<>();;

	
	@RabbitListener(queues = "${rabbitmq.consumer.data.queue}")
	public void recievedMessage(String message) {

		str_queue.add(message);
		log.info("Recieved Message From RabbitMQ: {}", message);
	}
	
	public static String dequeue() {
		return str_queue.remove();
	}
}
