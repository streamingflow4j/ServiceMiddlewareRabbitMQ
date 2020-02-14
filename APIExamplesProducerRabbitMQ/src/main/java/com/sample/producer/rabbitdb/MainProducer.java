package com.sample.producer.rabbitdb;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class MainProducer {
   
	private MainProducer() { }

	public static void main(final String... args) {
		@SuppressWarnings("resource")
		final AbstractApplicationContext context =	new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	    CachingConnectionFactory connectionFactory = (CachingConnectionFactory) context.getBean("connectionFactory");
	    
	    // define the range 
        int max = 40; 
        int min = 10; 
        int range = max - min + 1; 
	    
	    RabbitTemplate template = new RabbitTemplate(connectionFactory);
	    template.setRoutingKey("si.test.queue");
		String payload ="";
	    for (int i = 0; i < 5000; i++) {
	    	int rand = (int)(Math.random() * range) + min; 
	    	payload = "{\"contextElement\":{\"type\" : \"Termometer\",\"id\" : \""+1+"\",\"attributes\" : [{ \"name\" : \"temperature\",\"type\" : \"Double\",\"value\" : \""+rand+"\"}]}}";			    
	    	template.convertAndSend(payload);
	    }
	    context.registerShutdownHook();
        System.out.println("Producer_Rabbit");
	}
}

