package com.example.consumer.rabbitmq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


@Component
public class RabbitMQConsumer {
    
	private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);
	
	private static Queue<String> str_queue = new LinkedList<String>();;


	@RabbitListener(queues = "${rabbitmq.consumer.data.queue}")
	public void recievedMessage(Message message) throws IOException {
		str_queue.add(new String(message.getBody()));
		log.info("Recieved Message From RabbitMQ: {}", message);
	}
	
	public static String dequeue() {
		return str_queue.remove();
	}
}
