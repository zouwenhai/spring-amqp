package com.example.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author zouwenhai
 * @version v1.0
 * @date 2019/8/23 18:04
 * @work //TODO
 */
@Component
public class Consumer {

    @RabbitListener(queues = "cord")
    //@RabbitListener(queues = "cord", containerFactory="myFactory")
    public void processMessage(String msg) {
        System.out.format("Receiving Message: -----[%s]----- \n.", msg);
    }


    @RabbitListener(queues = "cord")
    @SendTo("cord")
    //@RabbitListener(queues = "cord", containerFactory="myFactory")
    public String  processMessage2(String msg) {
        System.out.format("Receiving Message: -----[%s]----- \n.", msg);
        return "已经接收到信息";
    }

}
