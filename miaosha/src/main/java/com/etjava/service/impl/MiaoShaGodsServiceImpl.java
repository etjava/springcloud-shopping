package com.etjava.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etjava.entity.MiaoShaGoods;
import com.etjava.mapper.MiaoShaGoodsMapper;
import com.etjava.service.IMiaoShaGoodsService;

@Service("miaoshaGoodsService")
public class MiaoShaGodsServiceImpl  extends ServiceImpl<MiaoShaGoodsMapper, MiaoShaGoods> implements IMiaoShaGoodsService{

	@Autowired
    private MiaoShaGoodsMapper miaoshaGoodsMapper;

	@Override
	public List<MiaoShaGoods> listAll(Map<String,Object> map) {
		return miaoshaGoodsMapper.listAll(map);
	}
	
	
}
