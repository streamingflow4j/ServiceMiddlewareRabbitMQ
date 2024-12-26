package com.service.middleware.config;


import com.service.middleware.cep.handler.MonitorEventHandler;
import com.service.middleware.cep.subscribe.MonitorEventSubscriber;
import com.service.middleware.listener.ConsumerServiceActivator;
import com.service.middleware.listener.ModelTransformer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MonitorEventHandler monitorEventHandler() {
        return new MonitorEventHandler();
    }

    @Bean
    ConsumerServiceActivator consumerServiceActivator(){
        return new ConsumerServiceActivator();
    }

    @Bean
    MonitorEventSubscriber monitorEventSubscriber(){
        return new MonitorEventSubscriber();
    }

    @Bean
    ModelTransformer modelTransformer(){
        return new ModelTransformer();
    }
}
