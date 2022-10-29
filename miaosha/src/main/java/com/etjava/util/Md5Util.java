package com.etjava.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * md5加密工具类
 * @create 2020-11-23 18:54
 */
public class Md5Util {

    private static  final String FRONT_SALT="3dfsty"; // 前端加密盐  要与前端保持一致

    private static final String BACK_SALT_BEFORE="3qx2dfdx3"; // 后端加密前置盐

    private static final String BACK_SALT_AFTER="ds2f3dsf5"; // 后端加密后置盐

    /**
     * md5加密
     * @param data
     * @return
     */
    public static String md5(String data){
        return DigestUtils.md5Hex(data);
    }

    /**
     * 前端加盐后md5加密
     * @param frontData
     * @return
     */
    public static String frontMd5(String frontData){
        return md5(FRONT_SALT+frontData);
    }

    /**
     * 后端加盐后md5加密
     * @param backData
     * @return
     */
    public static String backMd5(String backData){
        return md5(BACK_SALT_BEFORE+backData+BACK_SALT_AFTER);
    }

    public static void main(String[] args) {
        System.out.println("前端md5加密："+Md5Util.frontMd5("123456"));

        System.out.println("后端md5加密验证："+backMd5(frontMd5("123456")));
    }


}
