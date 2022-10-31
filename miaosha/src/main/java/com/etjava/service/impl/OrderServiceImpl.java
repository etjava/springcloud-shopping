package com.etjava.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etjava.entity.Order;
import com.etjava.mapper.OrderMapper;
import com.etjava.service.IOrderService;

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Override
	public Order findById(String id) {
		return orderMapper.findById(id);
	}

	@Override
	public Integer add(Order order) {
		return orderMapper.add(order);
	}

}
