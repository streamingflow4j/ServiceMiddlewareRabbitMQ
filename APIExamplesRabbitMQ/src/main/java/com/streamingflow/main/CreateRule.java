package com.streamingflow.main;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class CreateRule {
     
	private CreateRule() { }

public static void main(final String... args) throws InterruptedException {

		
		@SuppressWarnings("resource")
		final AbstractApplicationContext context =	new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	    CachingConnectionFactory connectionFactory = (CachingConnectionFactory) context.getBean("connectionFactory");

	    RabbitTemplate template = new RabbitTemplate(connectionFactory);
	    template.setRoutingKey("si.ceprule.queue");
	    template.setMandatory(true);

	    String payload = "";

	    payload = "{\"contextElement\":{\"type\" : \"RULECEP\",\"id\" : \"Rule4\","
	    		+ "\"attributes\" : [{ \"name\"  : \"RULE\","
	    		                    + "\"type\"  : \"String\","
	    		                    + "\"value\" : \"select temperature from Termometro.win:time(5 sec)\"}"
	    		                    + "{\"name\"  : \"QUEUE_1\","
	    	    		            + "\"type\"  : \"QUEUE\","
	    	    		            + "\"value\" : \"si.cep.queue\"}"
	    		                    + "]}}";
	    

	
	    template.convertAndSend(payload);
	    context.registerShutdownHook();
	}
}

