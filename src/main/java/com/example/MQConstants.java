package com.example;


/**
 * Created By WangYx Date: 2018/7/31 Description:
 */
public class MQConstants {


    /**
     * 一次性从broker里面取的待消费的消息的个数
     */
    public static final Integer DEFAULT_PREFETCH_COUNT = 8;
    /**
     * 对每个listener在初始化的时候设置的并发消费者的个数
     */
    public static final Integer DEFAULT_CONCURRENT = 4;
    public static final Integer DEFAULT_MAX_CONCURRENT = 4;


    public MQConstants() {}




}