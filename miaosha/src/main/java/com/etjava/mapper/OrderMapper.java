package com.etjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etjava.entity.Order;

public interface OrderMapper extends BaseMapper<Order> {

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
