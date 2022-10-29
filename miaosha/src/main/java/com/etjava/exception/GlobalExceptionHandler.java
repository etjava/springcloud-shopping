package com.etjava.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etjava.entity.R;

/**
 * 全局异常处理
 * @author etjav
 *
 */

@ControllerAdvice // 织入
@ResponseBody
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
    public R exceptionHandler(HttpServletRequest request,Exception e){
        System.out.println("全局异常捕获 ："+e);
        return R.error("服务端异常 <br /> "+e.getMessage()+"<br/>"+e.getStackTrace());
    }
}
