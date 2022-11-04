package com.etjava.entity;

import lombok.Data;

/**
 * 封装MQ消息的entity
 * 该消息封装类的序列化(字符串根Bean之间的转换) 自定义封装BeanUtil
 */
@Data
public class MQEntity {

    private User user;
    private Integer miaoShaGoodsId;

}
