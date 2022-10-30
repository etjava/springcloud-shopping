package com.etjava.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.etjava.interceptor.SysInterceptor;

/**
 * 处理跨域,请求访问拦截 捕获问题
 * @author etjav
 *
 */

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

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
	        //reqList.add("/miaoShaGoods/findAll");
	        
	        registry.addInterceptor(sysInterceptor()) // 添加拦截器
	                .addPathPatterns("/**") // 要拦截的访问请求
	                .excludePathPatterns(reqList); // 不进行拦截的请求
	}
    
    
    
}
