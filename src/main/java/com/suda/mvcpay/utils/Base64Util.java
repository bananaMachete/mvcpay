package com.suda.mvcpay.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;

/**
 * @author tim
 * @date 2017-12-25
 */
public class Base64Util {
    private static Log log  = LogFactory.getLog(Base64Util.class);
    // 将 s 进行 BASE64 编码
    public static String getBASE64(String s) {
        if (s == null){
            return null;
        }
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
    }

    // 将 BASE64 编码的字符串 s 进行解码
    public static String getFromBASE64(String s) { 
        if (s == null) {
            return null; 
        }
        BASE64Decoder decoder = new BASE64Decoder(); 
        try { 
            byte[] b = decoder.decodeBuffer(s); 
            return new String(b); 
        } catch (Exception e) {
            log.info("解码出错",e);
            return null;
        } 
    }
    
    public static void main(String[] args) {
        System.out.println(Base64Util.getBASE64("1"));
        System.out.println(Base64Util.getFromBASE64("MQ=="));
    }
}
