package com.streamingflow.main;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class CreateEvent {
     
	private CreateEvent() { }

public static void main(final String... args) throws InterruptedException {
        		
		@SuppressWarnings("resource")
		final AbstractApplicationContext context =	new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	    CachingConnectionFactory connectionFactory = (CachingConnectionFactory) context.getBean("connectionFactory");

	    RabbitTemplate template = new RabbitTemplate(connectionFactory);
	    template.setRoutingKey("si.ceprule.queue");
	    template.setMandatory(true);

	    String payload = "";
	    payload = "{\"contextElement\":{\"type\" : \"ADD_EVENT\",\"id\" : \"Termometro\","
	    		+ "\"attributes\" : [{ \"name\"  : \"id\","
	    		                    + "\"type\"  : \"String\","
	    		                    + "\"value\" : \"0\"}"
	    		                    + "{\"name\" : \"temperature\","
	    	    		            + "\"type\"  : \"Double\","
	    	    		            + "\"value\" : \"0\"}"
	    		                    + "]}}";
	
	    template.convertAndSend(payload);
	    context.registerShutdownHook();
	}
}
