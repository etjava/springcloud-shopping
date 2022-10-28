package com.etjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etjava.entity.User;
import com.etjava.service.IUserService;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录
     * @return
     */
    @RequestMapping("/login")
    public User login(String userName){
        return userService.findByUserName(userName);
    }
}
