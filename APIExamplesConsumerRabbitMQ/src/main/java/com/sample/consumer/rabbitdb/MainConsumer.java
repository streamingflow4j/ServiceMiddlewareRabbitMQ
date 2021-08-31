package com.sample.consumer.rabbitdb;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public final class MainConsumerMongodb {

	private MainConsumerMongodb() { }
	
	public static void main(final String... args) {

		@SuppressWarnings("resource")
		final AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		context.registerShutdownHook();
        System.out.println("ConsumerMongodb");
	}
}

