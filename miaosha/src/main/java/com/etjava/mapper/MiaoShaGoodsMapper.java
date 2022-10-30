package com.etjava.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etjava.entity.MiaoShaGoods;

public interface MiaoShaGoodsMapper extends BaseMapper<MiaoShaGoods> {

	List<MiaoShaGoods> listAll(Map<String,Object> map);
}
