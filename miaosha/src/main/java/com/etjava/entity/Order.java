package com.etjava.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@TableName("t_order")
@Data
public class Order implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private String id; // 订单编号

	    private User user; // 购买用户

	    private MiaoShaGoods miaoShaGoods; // 购买的秒杀商品

	    @JsonSerialize(using=CustomDateTimeSerializer.class)
	    private Date createTime; // 创建日期

	    private Date payDate; // 支付日期

	    private Integer count; // 购买数量

	    private double totalPrice; // 订单总金额

	    private String payMethod; // 支付方式

	    private Integer status; // 订单状态   0:订单生成  1：已支付  2 已发货  3：已收货

	
}
