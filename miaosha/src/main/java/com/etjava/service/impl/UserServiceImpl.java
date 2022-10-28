package com.etjava.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etjava.entity.User;
import com.etjava.mapper.UserMapper;
import com.etjava.service.IUserService;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUserName(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,userName);// lambda表达式 从User对象中获取username,第二个参数是传递过来的userName
        return userMapper.selectOne(queryWrapper);
    }
}
