package com.etjava.rabbitmq;

import com.etjava.config.RabbitMQConfig;
import com.etjava.entity.MQEntity;
import com.etjava.util.BeanUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发送MQ消息
 */
@Service
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     *
     * @param mqEntity
     */
    public void miaoShaGoodsMessage(MQEntity mqEntity){
        System.out.println("======================================= 发送秒杀消息到"+RabbitMQConfig.QUEUE_NAME+"队列中");
        // 将entity转成json格式字符串
        String msg = BeanUtil.beanToString(mqEntity);
        System.out.println("send msg:"+msg);

        // 发送消息
        amqpTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME,msg);
    }
}
