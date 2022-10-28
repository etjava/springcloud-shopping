package com.etjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etjava.entity.User;

/**
 *  用户Service接口
 */
public interface IUserService extends IService<User> {

    /**
     * 根据userName查询用户信息
     * @param userName
     * @return
     */
    User findByUserName(String userName);
}