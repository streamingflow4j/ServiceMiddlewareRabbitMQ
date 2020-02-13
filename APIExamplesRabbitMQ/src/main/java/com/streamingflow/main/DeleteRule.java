package com.streamingflow.main;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class DeleteRule {

private DeleteRule() { }

public static void main(final String... args) throws InterruptedException {
        
		@SuppressWarnings("resource")
		final AbstractApplicationContext context =	new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	    CachingConnectionFactory connectionFactory = (CachingConnectionFactory) context.getBean("connectionFactory");

	    RabbitTemplate template = new RabbitTemplate(connectionFactory);
	    template.setRoutingKey("si.ceprule.queue");
	    template.setMandatory(true);

	    String payload = "";
	    payload = "{\"contextElement\":{\"type\" : \"DEL_RULE\",\"id\" : \"DelRule3\","
	    		+ "\"attributes\" : [{ \"name\"  : \"RULE_ID\","
	    		                    + "\"type\"  : \"String\","
	    		                    + "\"value\" : \"43d380a2-77dd-46f9-802c-727c0abb823b\"}"
	    		                    + "]}}";
	
	    template.convertAndSend(payload);
	    context.registerShutdownHook();
	}
}
