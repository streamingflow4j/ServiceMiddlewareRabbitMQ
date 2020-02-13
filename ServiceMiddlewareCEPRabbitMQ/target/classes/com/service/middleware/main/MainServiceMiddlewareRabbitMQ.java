package com.service.middleware.main;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public final class MainServiceMiddlewareRabbitMQ {

	private MainServiceMiddlewareRabbitMQ() { }
	
	public static void main(final String... args) {
		@SuppressWarnings("resource")
		final AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		context.registerShutdownHook();
        System.out.println("ServiceMiddlewareRabbitMQ");
	}
	
}

