package com.etjava.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.etjava.interceptor.SysInterceptor;

/**
 * 处理跨域,请求访问拦截 捕获问题
 * @author etjav
 *
 */

@Configuration
public class WebAppConfig implements WebMvcConfigurer {
	
	@Value("${image_path}")
	private String imagePath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD");
    }
    
    @Bean
    public SysInterceptor sysInterceptor() {
    	return new SysInterceptor();
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		 List<String> reqList = new ArrayList<>();
	        reqList.add("/login");
	        reqList.add("/verifyCode/get"); // 前端获取验证码
	       // reqList.add("/register"); // 模拟创建用户请求 为了压力测试
	       // reqList.add("/miaoShaGoods/findAll"); // 测试获取全部秒杀商品接口
	        
	        registry.addInterceptor(sysInterceptor()) // 添加拦截器
	                .addPathPatterns("/**") // 要拦截的访问请求
	                .excludePathPatterns(reqList); // 不进行拦截的请求
	}
    
	
	/**
     * 虚拟路径映射
     * 映射图片存放路径(本地目录)
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 注意 addResourceLocations路径要以\或/ 结尾 否则映射不到
        registry.addResourceHandler("/image/**").addResourceLocations(imagePath);
    }
    
    
}
