package com.etjava.rabbitmq;

import com.etjava.config.RabbitMQConfig;
import com.etjava.constant.Constant;
import com.etjava.entity.MQEntity;
import com.etjava.entity.Order;
import com.etjava.entity.User;
import com.etjava.service.IMiaoShaGoodsService;
import com.etjava.service.IMiaoShaService;
import com.etjava.service.IOrderService;
import com.etjava.util.BeanUtil;
import com.etjava.util.RedisUtil;
import com.etjava.entity.vo.MiaoShaGoodsVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息接收者
 */
@Service
public class MQReceiver {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IMiaoShaGoodsService miaoShaGoodsService;

    @Autowired
    private IMiaoShaService miaoShaService;

    @Autowired
    private IOrderService orderService;

    // 监听某个队列的消息(获取队列的消息)
    // 由RabbitMQ自动获取队列消息时 直接处理了秒杀的库存等操作
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiverMsg(String msg){
        System.out.println("=============================== 监听"+RabbitMQConfig.QUEUE_NAME+" 消息队列 并处理订单及库存");
        MQEntity mqEntity = BeanUtil.stringToBean(msg, MQEntity.class);
        System.out.println("接收到的消息："+mqEntity);

        User user = mqEntity.getUser();
        Integer miaoShaGoodsId = mqEntity.getMiaoShaGoodsId();

        // 第二步 判断库存
        System.out.println("=========================== 判断库存是否充足");
        MiaoShaGoodsVo miaoshaGoods = miaoShaGoodsService.findById(miaoShaGoodsId);
        Integer stock = miaoshaGoods.getStock();
        if(stock<=0){
            System.out.println("秒杀商品库存不足");
            // 将redis中对应的商品库存改为true 表示库存已经被秒杀完  不能保证实时执行 因此需要在轮询时在去做次判断
            redisUtil.set(Constant.REDIS_GOODS_MIAOSHA_OVER_PREFIX,true);
            return;
        }
        // 第三步 判断是否重复秒杀
        System.out.println("=========================== 判断是否重复秒杀");
        Map<String,Object> map2 = new HashMap<>();
        map2.put("userId",user.getId());
        map2.put("miaoshaGoodsId",miaoshaGoods.getId());
        List<Order> orderList = orderService.findOrderWithUIDAndMiaoShaGoodsId(map2);
        if(orderList.size()!=0){
            System.out.println("已参加过当前商品的秒杀活动");
            // 缓存中应恢复之前的库存
            redisUtil.incr(Constant.REDIS_STOCK_PREFIX+miaoShaGoodsId + "", 1);
            return;
        }

        // 第四步 减库存 下订单 必须同一个事务
        System.out.println("=========================== 减库存 下订单");
        String orderId = miaoShaService.miaosha(user, miaoshaGoods);
        if(orderId!=null){
            System.out.println("秒杀成功");
        }else{
            System.out.println("秒杀失败,稍后再试");
        }
    }
}
