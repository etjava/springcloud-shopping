package com.etjava.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etjava.constant.Constant;
import com.etjava.entity.R;
import com.etjava.util.RedisUtil;

@RestController
@RequestMapping("/")
public class TokenController {

	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * 刷新Token 有效期
	 * 前端是将token放在header中传递过来的
	 * @param request
	 * @return
	 */
	@GetMapping("/refreshToken")
	public R refreshToken(HttpServletRequest request) {
		
		String token = request.getHeader("token");
		System.out.println("token剩余有效时间："+redisUtil.getExpire(Constant.REDIS_TOKEN_PREFIX+token));
        // 重置token的有效时间 key=prefix+token
        redisUtil.expire(Constant.REDIS_TOKEN_PREFIX,token,Constant.REDIS_TOKEN_EXPIRE);
        System.out.println("重置后token剩余有效时间："+redisUtil.getExpire(Constant.REDIS_TOKEN_PREFIX+token));
		// 只是刷新token的有效期 不需要将token重新传递到前端 因为前端保存的有
		return R.ok();
	}
}
