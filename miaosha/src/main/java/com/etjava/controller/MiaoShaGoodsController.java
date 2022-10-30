package com.etjava.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etjava.constant.Constant;
import com.etjava.entity.R;
import com.etjava.entity.vo.MiaoShaGoodsVo;
import com.etjava.service.IMiaoShaGoodsService;
import com.etjava.util.RedisUtil;

@RestController
@RequestMapping("/miaoShaGoods")
public class MiaoShaGoodsController {

	@Autowired
    private IMiaoShaGoodsService miaoShaGoodsService;

    @Autowired
    private RedisUtil redisUtil;
    
    /**
     * 查询所有秒杀商品
     * @return
     */
    @RequestMapping("/findAll")
    public R findAll(){
        List<MiaoShaGoodsVo> miaoShaGoodsList=null;
        Object o=redisUtil.get(Constant.REDIS_MIAOSHA_GOODS);
        if(o==null){
            System.out.println("从数据库里面查询");
            miaoShaGoodsList = miaoShaGoodsService.listAll(null);
            redisUtil.set(Constant.REDIS_MIAOSHA_GOODS,miaoShaGoodsList,Constant.REDIS_MIAOSHA_GOODS_EXPIRE);
        }else{
            System.out.println("从redis中取值 = "+o);
            miaoShaGoodsList = (List<MiaoShaGoodsVo>) o;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("data",miaoShaGoodsList);
        return R.ok(map);
    }
}
