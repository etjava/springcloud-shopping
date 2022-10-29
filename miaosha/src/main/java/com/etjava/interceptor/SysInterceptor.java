package com.etjava.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etjava.constant.Constant;
import com.etjava.util.RedisUtil;
import com.etjava.util.StringUtil;

/**
 * 请求拦截器
 * 过滤非法请求(没有token的请求)
 *
 * 需要在WebAppConfig中将其注入到spring中
 */
public class SysInterceptor implements HandlerInterceptor{

	@Autowired
    private RedisUtil redisUtil;
	
	// 执行目标方法之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        System.out.println("请求路径："+path);
        // 只对方法的请求进行拦截鉴权
        if(handler instanceof HandlerMethod){
           String token = request.getHeader("token");
            System.out.println("token = "+token);
            if(StringUtil.isEmpty(token)){
                System.out.println("token为空");
                throw new RuntimeException("签名验证不存在");
            }else{
                // 获取缓存中的token 根据key
                Object o = redisUtil.get(Constant.REDIS_TOKEN_PREFIX,token);
                if(o!=null){
                    // 验证成功
                    System.out.println("签名验证成功");
                    return true;
                }else{
                    System.out.println("签名验证失败");
                    throw new RuntimeException("签名验证失败");
                }
            }
        }else{
            // true 不拦截
            return true;
        }
    }
}
