package com.example.springamqp;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectSender {
        private static final String EXCHANGE_NAME = "direct_exchange";

        public static void main(String []args) throws IOException, TimeoutException {
            //create rabbit connection factory
            ConnectionFactory factory = new ConnectionFactory();
            //crate a connect to rabbit server
            Connection connection = factory.newConnection();
            //create channel
            Channel channel = connection.createChannel();
            String message = "this is a direct message";
            //send message to rabbit exchange
            channel.basicPublish(EXCHANGE_NAME, "direct_routing_key", null, message.getBytes());
            channel.close();
            factory.clone();


        }

}
