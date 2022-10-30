package com.etjava.entity.vo;

import com.etjava.entity.MiaoShaGoods;

import lombok.Data;

@Data
public class MiaoShaGoodsVo extends MiaoShaGoods{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer miaoShaStatus=0; // 秒杀状态
    private Integer remainSecond=0; // 剩余多少秒
    private Integer remainEndSecond=0; // 秒杀结束 剩余多少秒
}
