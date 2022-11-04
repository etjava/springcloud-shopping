package com.etjava.runner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.etjava.constant.Constant;
import com.etjava.entity.vo.MiaoShaGoodsVo;
import com.etjava.service.IMiaoShaGoodsService;
import com.etjava.util.RedisUtil;

/**
 * 系统启动时加载配秒杀商品库存信息及是否秒杀完的标识
 * 类似springmvc中的listener监听 component
 */
@Component("startupRunner")
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IMiaoShaGoodsService miaoShaGoodsService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("系统启动时加载秒杀商品信息 - 商品ID、库存");
        List<MiaoShaGoodsVo> miaoShaGoodsList = miaoShaGoodsService.listAll(null);

        for(MiaoShaGoodsVo vo : miaoShaGoodsList){
            System.out.println("秒杀商品ID "+vo.getId()+" 库存 "+vo.getStock());
            // 放到redis中  id-stock
            redisUtil.set2(Constant.REDIS_STOCK_PREFIX,vo.getId()+"",vo.getStock()+"");
            redisUtil.set(Constant.REDIS_GOODS_MIAOSHA_OVER_PREFIX,vo.getId()+"",false);// 默认没有被秒杀完

//            boolean f = (boolean)redisUtil.get(Constant.REDIS_GOODS_MIAOSHA_OVER_PREFIX, vo.getId() + "");
//            System.out.println("====================="+f);
        }
    }
}

