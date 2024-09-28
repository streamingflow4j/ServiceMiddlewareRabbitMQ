package com.example.consumer.rabbitmq.listener.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.consumer.rabbitmq.listener.RabbitMQConsumer;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(value = "/test")
public class RestRabbitController {

	
	 @Autowired
	 private RabbitMQConsumer restRabbit;
 
	 @ResponseStatus(HttpStatus.OK)	
	 @GetMapping(value = "/get")
     public ResponseEntity getConsumerRelease() {
		 return new ResponseEntity<>(restRabbit.dequeue(), HttpStatus.OK);
	 }
	
}
