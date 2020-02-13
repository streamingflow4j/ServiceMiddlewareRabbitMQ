package com.streamingflow.main;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class UpdateRule {
     
	private UpdateRule() { }

public static void main(final String... args) throws InterruptedException {
		
		@SuppressWarnings("resource")
		final AbstractApplicationContext context =	new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	    CachingConnectionFactory connectionFactory = (CachingConnectionFactory) context.getBean("connectionFactory");

	    RabbitTemplate template = new RabbitTemplate(connectionFactory);
	    template.setRoutingKey("si.ceprule.queue");
	    template.setMandatory(true);

	    String payload = "";
	    payload = "{\"contextElement\":{\"type\" : \"EDIT_RULECEP\",\"id\" : \"Rule3\","
			    		+ "\"attributes\" : [{ \"name\"  : \"RULE_ID\","
		                + "\"type\"  : \"String\","
		                + "\"value\" : \"43d380a2-77dd-46f9-802c-727c0abb823b\"}"
		                + "{\"name\"  : \"RULE\","
			            + "\"type\"  : \"String\","
			            + "\"value\" : \"select temperature from Termometro.win:time(10 sec)\"}"
			            + "{\"name\"  : \"QUEUE_1\","
    		            + "\"type\"  : \"QUEUE\","
    		            + "\"value\" : \"si.cep.queue2\"}"
	                    + "]}}";
	
	    template.convertAndSend(payload);
	    context.registerShutdownHook();
	}
}
