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
    @SuppressWarnings("unchecked")
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
    
    @RequestMapping("/findById")
    public R findById(Integer id){
        MiaoShaGoodsVo goods = miaoShaGoodsService.findById(id);
        Map<String,Object> map=new HashMap<>();

        Integer miaoShaStatus=0; // 秒杀状态
        Integer remainBeginSecond=0; // 剩余多少秒开始秒杀
        Integer remainEndSecond=0; // 秒杀结束 剩余多少秒

        // 转成时间戳
        long startTime = goods.getStartTime().getTime();
        long endTime = goods.getEndTime().getTime();
        long currentTime = System.currentTimeMillis();
        System.out.println("startTime "+startTime);
        System.out.println("endTime "+endTime);
        System.out.println("currentTime "+currentTime);

        // 判断秒杀状态
        if(currentTime<startTime){ // 秒杀还没开始
            miaoShaStatus=0; // 0 未开始秒杀，1 秒杀进行中 2 秒杀已结束
            remainBeginSecond=(int)(startTime-currentTime)/1000; // 剩余秒杀开始时间
            remainEndSecond=(int)(endTime-currentTime)/1000;// 距离结束剩余时间
        }else if (currentTime>endTime){ // 秒杀已结束
            miaoShaStatus=2;
            remainBeginSecond=-1; // 秒杀已结束
            remainEndSecond=-1; // 秒杀已结束
        }else{
            miaoShaStatus=1 ; // 秒杀进行中
            remainBeginSecond=0; // 距离秒杀开始时间
            remainEndSecond=(int)(endTime-currentTime)/1000;// 距离结束剩余时间
        }

        goods.setRemainBeginSecond(remainBeginSecond);
        goods.setMiaoShaStatus(miaoShaStatus);
        goods.setRemainEndSecond(remainEndSecond);

        map.put("data",goods);
        return R.ok(map);
    }
}
