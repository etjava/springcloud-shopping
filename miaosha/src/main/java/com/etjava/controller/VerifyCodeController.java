package com.etjava.controller;


import com.etjava.constant.Constant;
import com.etjava.entity.R;
import com.etjava.entity.User;
import com.etjava.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * 订单控制器
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2020-12-13 11:20
 */
@Controller
@RequestMapping("/verifyCode")
public class VerifyCodeController {



    @Autowired
    private RedisUtil redisUtil;


    /**
     * 返回验证码图片
     * @param miaoShaGoodsId
     * @return
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public R get(HttpServletResponse response,String token, Integer miaoShaGoodsId){
        System.out.println("token:"+token);
        System.out.println("miaoShaGoodsId:"+miaoShaGoodsId);
    /*    String token = request.getParameter("token");
        System.out.println("token:"+token);*/
        Object o=redisUtil.get(Constant.REDIS_TOKEN_PREFIX,token);
        if(o==null){
            return null;
        }
        if(miaoShaGoodsId==null || miaoShaGoodsId <=0){
            return null;
        }
        try {
            BufferedImage image = this.createVerifyCodeImage(((User) o).getId(), miaoShaGoodsId);
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e){
            e.printStackTrace();
            return R.error("服务端异常");
        }
    }

    private BufferedImage createVerifyCodeImage(Integer userId,Integer miaoShaGoodsId) {
        int width=80;
        int height=32;
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g=image.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0,0,width,height);
        g.setColor(Color.black);
        g.drawRect(0,0,width-1,height-1);
        Random rdm=new Random();
        for(int i=0;i<50;i++){
            int x=rdm.nextInt(width);
            int y=rdm.nextInt(height);
            g.drawOval(x,y,0,0);
        }

        String verifyCode=createVerifyCode();
        System.out.println("验证："+verifyCode);
        g.setColor(new Color(154, 47, 189)); // 生成的图片颜色
        g.setFont(new Font("Candara",Font.BOLD,24));
        g.drawString(verifyCode+"=",8,24);
        g.dispose();

        int rnd=calc(verifyCode);
        System.out.println("rnd:"+rnd);
        redisUtil.set(Constant.REDIS_VERIFYCODE_PREFIX,userId+","+miaoShaGoodsId,rnd,Constant.REDIS_VERIFYCODE_EXPIRE);

        return image;

    }

    /**
     * 计算表达式
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        ScriptEngineManager manager=new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            return (int) engine.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }


    private static char[] ops=new char[]{'+','-','*'};

    /**
     * + - 运算
     * @return
     */
    private static String createVerifyCode() {
        Random rdm=new Random();
        int num1=rdm.nextInt(10);
        int num2=rdm.nextInt(10);
        Character op1=ops[rdm.nextInt(3)];
        
        String exp = "";
        
        // 禁止为负数运算
        if(op1.equals('-') && num1 < num2) {
        	num1 = num1^num2;
        	num2 = num1^num2;
        	num1 = num1^num2;
        	exp=num1+String.valueOf(op1)+num2;
        }else {
        	exp=num1+String.valueOf(op1)+num2;
        }
        
        System.out.println("exp:"+exp);        
        
        return exp;
    }

    public static void main(String[] args) {
    	for(int i=0;i<100;i++) {
    		String verifyCode=createVerifyCode();
    		System.out.println(calc(verifyCode));
    	}
    	
    	
    	
    	/*
    	 
    	int a = 10,b = 9;
    	a = a^b; // 3
    	b = a^b; // 10
    	a = a^b; // 9
    	
    	00000011 = 3
    	00001010 = 10
    	00001001 = 9
    	
    	System.out.println(a+"  "+b);
    	 
    	 */
    	
    	
    }
}
