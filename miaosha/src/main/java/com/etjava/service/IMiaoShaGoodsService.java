package com.etjava.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etjava.entity.MiaoShaGoods;
import com.etjava.entity.vo.MiaoShaGoodsVo;

public interface IMiaoShaGoodsService extends IService<MiaoShaGoods>{

	public List<MiaoShaGoodsVo> listAll(Map<String,Object> map);
	
	public MiaoShaGoodsVo findById(Integer id);
}
