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


    //创建AMQP连接
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(AMQP_URI);
        //配置通道缓存的大小(默认值为25)
        connectionFactory.setChannelCacheSize(5);
        return connectionFactory;
    }

    //声明AmqpAdmin,使用AmqpAdmin初始化队列、交换器以及路由键
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    //声明一个Direct类型的交换器
//Exchange类所在的包目录中，包含FanoutExchange、DirectExchange等其他的交换器类型，查看接口的实现类可以看出，声明是 Interface for all exchanges.
    @Bean
    DirectExchange exchange(AmqpAdmin amqpAdmin) {
        DirectExchange exchange = new DirectExchange("direct_exchange", true, false);
        amqpAdmin.declareExchange(exchange);
        log.info("declare rabbit exchange [{}];", exchange);
        return exchange;
    }


    //初始化消息处理类
    //对接收的消息做业务处理的类，需要实现ChannelAwareMessageListener接口的onMessage(Message message, Channel channel)方法。AbstractAdaptableMessageListener类介绍
    @Bean
    MessageReceive weyneEventListener() {
        return new MessageReceive();
    }

    //创建队列
    @Bean
    Queue listenerQueueBinding(
            AmqpAdmin amqpAdmin) {
        Queue queue = new Queue("direct_queue");
        amqpAdmin.declareQueue(queue);
        log.info("declare rabbit queue [{}];", queue);
        return queue;
    }

    //创建一个队列与exchange的绑定关系
    @Bean
    Binding initBindings(AmqpAdmin amqpAdmin, Queue queue, DirectExchange exchange) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("direct_routing_key");
        amqpAdmin.declareBinding(binding);
        return binding;
    }

    //初始化一个MessageListener，用于监听指定的消息队列，当有消息来时，通过container.setMessageListener(listener);指定自己定义的消息处理类进行消息处理。
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
