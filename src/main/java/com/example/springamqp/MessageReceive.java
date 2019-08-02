package com.example.springamqp;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

@Slf4j
public class MessageReceive  implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {


        String str = new String(message.getBody());
        log.info("message:{}",message);
        log.info("message:{}",  message.toString());
        log.info("str:{}",str);
    }
}
