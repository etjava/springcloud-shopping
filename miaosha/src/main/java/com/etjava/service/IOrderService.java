package com.etjava.service;

import java.util.List;
import java.util.Map;

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
	
	/**
	 * 	根据用户ID和秒杀商品ID查询订单
	 * @param map
	 * @return
	 */
	List<Order> findOrderWithUIDAndMiaoShaGoodsId(Map<String,Object> map);
}
