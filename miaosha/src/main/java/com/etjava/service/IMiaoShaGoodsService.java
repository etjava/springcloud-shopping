package com.etjava.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etjava.entity.MiaoShaGoods;

public interface IMiaoShaGoodsService extends IService<MiaoShaGoods>{

	public List<MiaoShaGoods> listAll(Map<String,Object> map);
}
