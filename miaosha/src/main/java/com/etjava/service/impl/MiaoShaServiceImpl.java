package com.etjava.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etjava.entity.MiaoShaGoods;
import com.etjava.entity.Order;
import com.etjava.entity.User;
import com.etjava.mapper.MiaoShaGoodsMapper;
import com.etjava.mapper.OrderMapper;
import com.etjava.service.IMiaoShaService;
import com.etjava.util.DateUtil;

@Service("miaoShaService")
public class MiaoShaServiceImpl implements IMiaoShaService {

	@Autowired
    private MiaoShaGoodsMapper miaoShaGoodsMapper;

    @Autowired
    private OrderMapper orderMapper;
	
	@Override
	public String miaosha(User user, MiaoShaGoods miaoShaGoods) {
		// 库存-1操作
        Integer res = miaoShaGoodsMapper.reduceStock(miaoShaGoods.getId());
        if(res==0){ // 减库存失败
            return null;
        }
        // 生成订单
        Order order = new Order();
        order.setId(DateUtil.getCurrentDateStr());
        order.setUser(user);
        order.setMiaoShaGoods(miaoShaGoods);
        order.setCount(1); // 每次秒杀只能是一个
        order.setTotalPrice(miaoShaGoods.getPrice());
        order.setPayMethod("银联卡支付");
        order.setStatus(0); // 0:订单生成  1：已支付  2 已发货  3：已收货
        Integer res2 = orderMapper.add(order);
        if(res2==0){
            return null; // 订单生成失败
        }
        return order.getId();
	}

}
