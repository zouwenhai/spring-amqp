package com.example.springamqp;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WeyneEventAutoConfiguration {


    private static final String AMQP_URI = "amqp://guest:guest@localhost:5672";


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
         connectionFactory.setUri(AMQP_URI);
        //配置通道缓存的大小(默认值为25)
        connectionFactory.setChannelCacheSize(5);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }



    @Bean
    DirectExchange exchange(AmqpAdmin amqpAdmin) {
        DirectExchange exchange = new DirectExchange("direct_exchange", true, false);
        amqpAdmin.declareExchange(exchange);
        log.info("declare rabbit exchange [{}];", exchange);
        return exchange;
    }


    @Bean
    MessageReceive weyneEventListener() {
        return new MessageReceive();
    }


    @Bean
    Queue listenerQueueBinding(
            AmqpAdmin amqpAdmin) {
        Queue queue = new Queue("direct_queue");
        amqpAdmin.declareQueue(queue);
        log.info("declare rabbit queue [{}];", queue);
        return queue;
    }


    @Bean
    Binding initBindings(AmqpAdmin amqpAdmin, Queue queue, DirectExchange exchange){
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("direct_routing_key");
        amqpAdmin.declareBinding(binding);
        return binding;
    }


    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(
            ConnectionFactory connectionFactory,
            MessageReceive listener,
            Queue queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queue);
        container.setMessageListener(listener);
        return container;
    }
}
