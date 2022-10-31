package com.etjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etjava.constant.Constant;
import com.etjava.entity.R;
import com.etjava.entity.User;
import com.etjava.service.IUserService;
import com.etjava.util.Md5Util;
import com.etjava.util.RedisUtil;
import com.etjava.util.StringUtil;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private IUserService userService;
    
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户登录
     * @return
     */
    @RequestMapping("/login")
    public R login(@RequestBody User user){
    	
    	if(user==null) {
    		return R.error();
    	}
    	
    	if(StringUtil.isEmpty(user.getUsername())) {
    		return R.error("用户名不能为空");
    	}
    	
    	if(StringUtil.isEmpty(user.getPassword())) {
    		return R.error("密码不能为空");
    	}
    	User user2 = userService.findByUserName(user.getUsername());
    	
    	if(user2==null) {
    		return R.error("账号信息不存在");
    	}
    	if(!user2.getPassword().equals(Md5Util.backMd5(user.getPassword()))) {
    		return R.error("密码错误");
    	}
    	// 登录成功需要生成token并保存到redis中 - 前后端分离
    	String token = StringUtil.uuid();
    	redisUtil.set(Constant.REDIS_TOKEN_PREFIX,token,user2,Constant.REDIS_TOKEN_EXPIRE);
        return R.ok(token);// 将token传递到前端
    }
}
