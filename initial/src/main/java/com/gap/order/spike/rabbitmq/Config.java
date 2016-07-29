package com.gap.order.spike.rabbitmq;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {

    public final static String rrBMCRoutingKey = "*.1.*";
    public final static String tnrBMCRoutingKey = "*.5.*";


    private Map<String, Object> deadLetterArguments() {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", "spring-boot-exchange");
        args.put("x-dead-letter-routing-key", "");

        args.put("x-message-ttl", 5000);
        return args;
    }

    @Autowired
    private ConnectionFactory cachingConnectionFactory;

    @Bean
    Queue deadLetterQueue() {
        return new Queue("deadLetterQueue", false);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    Queue tnrQueue() {
        return new Queue("tnrQueue", true, true, true, deadLetterArguments());
    }

    @Bean
    Queue rrQueue() {
        return new Queue("rrQueue", true, true, true, deadLetterArguments());
    }


    @Bean
    TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
    Binding tnrQueueBinding(@Qualifier("tnrQueue") Queue queue, TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(tnrBMCRoutingKey);
    }

    @Bean
    Binding deadLetterQueueBinding(@Qualifier("deadLetterQueue") Queue queue, TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with("deadLetterQueue");
    }

    @Bean
    Binding rrQueueBinding(@Qualifier("rrQueue") Queue queue, TopicExchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with(rrBMCRoutingKey);
    }
}
