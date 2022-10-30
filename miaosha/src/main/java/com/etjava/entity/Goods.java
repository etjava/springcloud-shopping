package com.etjava.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 	商品实体
 * @author etjav
 *
 */
@TableName("t_goods")
@Data
public class Goods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private double price;
	private String image;
	private Integer stock; // 库存
	private String detail; // 商品详情
}
