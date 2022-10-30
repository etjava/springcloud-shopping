package com.etjava.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etjava.entity.MiaoShaGoods;
import com.etjava.entity.vo.MiaoShaGoodsVo;

public interface MiaoShaGoodsMapper extends BaseMapper<MiaoShaGoods> {

	List<MiaoShaGoodsVo> listAll(Map<String,Object> map);
}
