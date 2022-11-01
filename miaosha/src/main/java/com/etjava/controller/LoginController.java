package com.etjava.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
    
    /**
     * 	伪注册用户 - 压力测试使用
     *	注册一万个用户并提交到redis中
     */
    
    @RequestMapping("/register")
    public R register() {
    	for(int i=0;i<10000;i++) {
    		User u = new User();
    		u.setUsername("user"+i);
    		u.setPassword("37cbc2f0be822f5ab96485ac11f3dc98");
    		// 生成token
    		String token = StringUtil.uuid();
    		// 添加到数据库
    		userService.save(u);
    		
    		// 添加到redis
    		redisUtil.set(Constant.REDIS_TOKEN_PREFIX,token,u,Constant.REDIS_TOKEN_EXPIRE);
    		
    		// 将用户信息写入到文件中 给Jmeter测试使用
    		addUserFile(u.getUsername(),token);
    	}
    	return R.ok();
    }

    /**
     * 	将用户信息写入到文件中 key,value格式
     * @param username
     * @param token
     */
	private void addUserFile(String username, String token) {
		String str = username+","+token+"\r\n";
		try {
			FileWriter fw = new FileWriter(new File("D://users.txt"),true); // true表示追加文件内容
			BufferedWriter bw = new BufferedWriter(fw,16384);
			bw.write(str);// 换行插入
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
