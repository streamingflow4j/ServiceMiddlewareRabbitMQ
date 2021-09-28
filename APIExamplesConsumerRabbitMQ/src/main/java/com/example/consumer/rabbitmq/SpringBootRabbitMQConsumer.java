package com.example.consumer.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootRabbitMQConsumer {

	public static void main(String[] args) {
		System.setProperty( "server.port","8081");
		SpringApplication.run(SpringBootRabbitMQConsumer.class, args);
	}

}
