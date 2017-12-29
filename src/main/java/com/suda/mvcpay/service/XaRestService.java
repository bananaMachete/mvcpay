package com.suda.mvcpay.service;
/**
 * @author tim
 * @date 2017-12-20
 */
public interface XaRestService {
    /**
     * 请求消息
     * @param openpl_appsecret 用户密钥
     * @param privateKey 用户私钥
     * @param methodname 调用方法名称
     * @param jsonString 发送的消息报文
     * @return 返回信息
     */
    public String requestOpenFlatMessage(String openpl_appsecret, String privateKey, String methodname, String jsonString);


    /**
     * 解密数据
     * @param openpl_appsecret 用户密钥
     * @param jsonString  返回的原消息报文
     * @return 返回信息
     */
    public String decryptOpenFlatMessage(String openpl_appsecret, String privateKey, String jsonString);
}
