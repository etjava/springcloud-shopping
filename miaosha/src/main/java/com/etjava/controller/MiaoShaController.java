package com.etjava.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etjava.constant.Constant;
import com.etjava.entity.Order;
import com.etjava.entity.R;
import com.etjava.entity.User;
import com.etjava.entity.vo.MiaoShaGoodsVo;
import com.etjava.service.IMiaoShaGoodsService;
import com.etjava.service.IMiaoShaService;
import com.etjava.service.IOrderService;
import com.etjava.util.RedisUtil;
import com.etjava.util.StringUtil;

/**
 * 执行秒杀业务controller
 */
@RestController
@RequestMapping("/miaosha")
public class MiaoShaController {

	
	@Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IMiaoShaGoodsService miaoShaGoodsService;
    
    @Autowired
    private IMiaoShaService miaoShaService;
    
    @Autowired
    private IOrderService orderService;
    
    /**
     * 	执行秒杀
     * @param request
     * @param miaoShaGoodsId
     * @return
     */
    @RequestMapping("/exec")
    public R exec(HttpServletRequest request,Integer miaoShaGoodsId ){
        // 第一步 获取当前登录用户 - token
        String token = request.getHeader("token");
        User u = null;
        if(StringUtil.isNotEmpty(token)){
            u = (User) redisUtil.get(Constant.REDIS_TOKEN_PREFIX,token);
            System.out.println("当前登录用户"+u);
        }else{
           return R.error("登录信息异常 不能参与秒杀");
        }
        if(u==null){
            return R.error("登录信息异常 不能参与秒杀");
        }
        // 第二步 判断库存
        MiaoShaGoodsVo miaoshaGoods = miaoShaGoodsService.findById(miaoShaGoodsId);
        Integer stock = miaoshaGoods.getStock();
        if(stock<=0){
            return R.error("秒杀商品库存不足");
        }
        // 第三步 判断是否重复秒杀
        Map<String,Object> orderMap = new HashMap<>();
        orderMap.put("userId",u.getId());
        orderMap.put("miaoshaGoodsId", miaoshaGoods.getId());
        List<Order> orderList = orderService.findOrderWithUIDAndMiaoShaGoodsId(orderMap);
        if(orderList.size()!=0) {
        	return R.error("已参加过当前商品的秒杀活动");
        }
        
        // 第四步 减库存 下订单 必须同一个事务
        String orderId = miaoShaService.miaosha(u, miaoshaGoods);
        if(orderId!=null){
            Map<String,Object> map = new HashMap<>();
            map.put("orderId",orderId);
            return R.ok(map);
        }else{
           return R.error("秒杀失败,稍后再试");
        }
    }
}
