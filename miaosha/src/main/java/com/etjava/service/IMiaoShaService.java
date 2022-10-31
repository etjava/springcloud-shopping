package com.etjava.service;

import com.etjava.entity.MiaoShaGoods;
import com.etjava.entity.User;

public interface IMiaoShaService {

	 /**
     * 	执行秒杀
     *
     */
    String miaosha(User user, MiaoShaGoods miaoShaGoods);
}
