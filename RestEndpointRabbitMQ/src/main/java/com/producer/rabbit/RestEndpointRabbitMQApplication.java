package com.producer.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestEndpointRabbitMQApplication {

	public static void main(String[] args) {
		System.setProperty( "server.port","8082");
		SpringApplication.run(RestEndpointRabbitMQApplication.class, args);
	}

}
