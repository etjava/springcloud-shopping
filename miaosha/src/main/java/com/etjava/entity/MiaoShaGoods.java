package com.etjava.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 	秒杀商品实体
 * @author etjava
 *
 */
public class MiaoShaGoods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	// 关联商品 select=false 查询时不显示
	@TableField(select = false)
	private Goods goods; 
	
	private double price;// 秒杀价格
	
	private Integer stock; // 秒杀商品库存
	
	// JSON 序列化时间
	@JsonSerialize(using=CustomDateTimeSerializer.class)
	private Date startTime; // 秒杀开始时间
	
	@JsonSerialize(using=CustomDateTimeSerializer.class)
	private Date endTime; // 秒杀结束时间

	
	
}
