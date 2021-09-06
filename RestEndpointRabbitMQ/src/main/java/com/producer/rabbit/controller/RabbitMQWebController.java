package com.producer.rabbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.producer.rabbit.model.Entity;
import com.producer.rabbit.service.RabbitMQSender;

@RestController
@RequestMapping(value = "/rabbitmq")
public class RabbitMQWebController {

	@Autowired
	RabbitMQSender rabbitMQSender;

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/data/create")
	public String newData(@RequestBody Entity entity) throws JsonProcessingException {	
		rabbitMQSender.sendData(entity);
		return "Message sent to the RabbitMQ JavaInUse Successfully";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/event/create")
	public String newEvent(@RequestBody Entity entity) throws JsonProcessingException {
		rabbitMQSender.sendRule(entity);
		return "Message sent to the RabbitMQ JavaInUse Successfully";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/rule/create")
	public String createRule(@RequestBody Entity entity) throws JsonProcessingException {
		rabbitMQSender.sendRule(entity);
		return "Message sent to the RabbitMQ JavaInUse Successfully";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = "/rule/update")
	public String updateRule(@RequestBody Entity entity) throws JsonProcessingException {
		rabbitMQSender.sendRule(entity);
		return "Message sent to the RabbitMQ JavaInUse Successfully";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(value = "/rule/delete")
	public String deleteRule(@RequestBody Entity entity) throws JsonProcessingException {
		rabbitMQSender.sendRule(entity);
		return "Message sent to the RabbitMQ JavaInUse Successfully";
	}
}
