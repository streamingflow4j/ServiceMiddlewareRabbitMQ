package com.service.middleware.main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;




public final class MainServiceMiddlewareRabbitMQ {
	
	private static final Logger log = LoggerFactory.getLogger(MainServiceMiddlewareRabbitMQ.class);

	private MainServiceMiddlewareRabbitMQ() { }
	
	public static void main(final String... args) {
		log.info("Before Starting application");
		@SuppressWarnings("resource")
		final AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		context.registerShutdownHook();
	     log.debug("Starting my application in debug with {} args", args.length);
	     log.info("Starting my application with {} args.", args.length); 
		//log.info("Service started ===>> ", MainServiceMiddlewareRabbitMQ.class.getName());
	}
	
}

