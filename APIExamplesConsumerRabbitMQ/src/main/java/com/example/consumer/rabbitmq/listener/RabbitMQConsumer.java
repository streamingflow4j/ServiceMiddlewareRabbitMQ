package com.example.consumer.rabbitmq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class RabbitMQConsumer {

	@RabbitListener(queues = "${rabbitmq.consumer.data.queue}")
	public void recievedMessage(String message) {
		System.out.println("Recieved Message From RabbitMQ: " + message);
	}
}
