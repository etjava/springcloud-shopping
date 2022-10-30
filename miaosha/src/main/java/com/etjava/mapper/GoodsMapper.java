package com.etjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etjava.entity.Goods;

public interface GoodsMapper extends BaseMapper<Goods>{

	Goods findById(Integer id);
}
