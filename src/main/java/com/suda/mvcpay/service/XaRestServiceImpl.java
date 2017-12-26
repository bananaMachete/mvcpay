package com.suda.mvcpay.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.suda.mvcpay.utils.FormatUtils;
import com.suda.mvcpay.utils.HttpNetProvider;
import com.suda.mvcpay.utils.MessageErrorCode;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @author tim
 * @date 2017-12-20
 * @ps 向西银请求数据
 */
@Service
public class XaRestServiceImpl implements XaRestService {

    @Override
    public String requestOpenFlatMessage(String openpl_appsecret, String privateKey, String methodname,String jsonString){
        JSONObject jsonres = new JSONObject();
        if (FormatUtils.formatCorrect(openpl_appsecret, privateKey, jsonString)) {
            System.out.println(MessageErrorCode.REQUEST_MESSAGE_FORMAT_ERROR);
            jsonres.put(MessageErrorCode.RESPONSE_MESSAGE_ERROE, MessageErrorCode.REQUEST_MESSAGE_FORMAT_ERROR);
            return jsonres.toString();
        }
        // 加密并格式化发送消息
        String postMessage = FormatUtils.encryptMessageByPrivateKey(openpl_appsecret, privateKey, jsonString);
        // 发送消息为空
        if (FormatUtils.stringIsNull(postMessage)) {
            System.out.println(MessageErrorCode.POST_MESSAGE_NULLL_ERROR);
            jsonres.put(MessageErrorCode.RESPONSE_MESSAGE_ERROE, MessageErrorCode.POST_MESSAGE_NULLL_ERROR);
            return jsonres.toString();
        }
        // 向西银惠付发送消息
        //String url = PropsUtil.getProperty(methodname);
        String url = "http://weixintest.xacbank.com.cn:9998/api/OpenPlatForm/scanNative";
        System.out.println("SEND URL: " + url);
        if (FormatUtils.stringIsNull(url)) {
            System.out.println(MessageErrorCode.POST_MESSAGE_URL_ERROR);
            jsonres.put(MessageErrorCode.RESPONSE_MESSAGE_ERROE, MessageErrorCode.POST_MESSAGE_URL_ERROR);
            return jsonres.toString();
        }
        byte[] data = null;
        try {
            data = HttpNetProvider.doPostBytes(url, postMessage, "application/json");
        } catch (Exception e) {
            System.out.println(MessageErrorCode.POST_MESSAGE_RETURN_ERROR);
            jsonres.put(MessageErrorCode.RESPONSE_MESSAGE_ERROE, MessageErrorCode.POST_MESSAGE_RETURN_ERROR);
            return jsonres.toString();
        }
        String returnMessage = null;
        try {
            returnMessage = new String(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("return message is : " + returnMessage);
        jsonres = FormatUtils.handleReturnMessage(openpl_appsecret, privateKey, returnMessage);
        System.out.println("return message for bussiness   " + jsonres.toString());
        return jsonres.toString();
    }

    /**
     * 解密数据
     * @param openpl_appsecret 用户密钥
     * @param privateKey  西银惠付返回的原消息报文
     * @return 返回信息
     */
       @Override
       public String decryptOpenFlatMessage(String openpl_appsecret, String privateKey, String encryptMessage) {
        System.out.println("format start");
        JSONObject re = FormatUtils.handleReturnMessage(openpl_appsecret, privateKey, encryptMessage);
        System.out.println("format is :" + re.toString());
        System.out.println("format end");
        return re.toString();
    }

}
