package com.etjava.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME="ETJAVA";

    /**
     * 简单模式 （一个生产者一个消费者）
     * 这里不需要交换机
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME);
    }

}
