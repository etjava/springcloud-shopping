package com.etjava.mapper;

import java.util.List;
import java.util.Map;

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
	
	/**
	 * 	根据用户ID和秒杀商品ID查询订单
	 * @param map
	 * @return
	 */
	List<Order> findOrderWithUIDAndMiaoShaGoodsId(Map<String,Object> map);
}
