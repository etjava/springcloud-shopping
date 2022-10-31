package com.etjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etjava.entity.Order;

public interface IOrderService extends IService<Order>{

	/**
	 *  	根据ID查询订单
	 * @param id
	 * @return
	 */
	Order findById(String id);
	
	/**
	 * 生成订单
	 * @param order
	 * @return
	 */
	Integer add(Order order);
}
