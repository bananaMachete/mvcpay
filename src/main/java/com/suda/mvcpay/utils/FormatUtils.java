package com.suda.mvcpay.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * 格式化工具类
 * @author HP
 *
 */
public class FormatUtils {
	private static Log log  = LogFactory.getLog(FormatUtils.class);
	
	static BASE64Decoder decoder = new BASE64Decoder(); 
	
	static BASE64Encoder encoder = new BASE64Encoder();
	
	/**
	 * 根据私钥加密消息字符串
	 * @param secretkey
	 * @param privateKey
	 * @param jsonString
	 * @return
	 */
	public static String encryptMessageByPrivateKey(String secretkey,String privateKey,String jsonString){
		log.info("======================================encryptMessage start=====================================");
		log.info("secretkey is "+secretkey+" ,privateKey "+privateKey+" ,data "+jsonString);
		String formatString = formatInputJson(jsonString);
		try {
			String hmac = HMACSHA256(formatString.getBytes("UTF-8"),secretkey.getBytes("UTF-8"));
			log.info("HMACSHA256 encryptMessage is "+hmac);
			byte[] encodedData = RSAUtils.encryptByPrivateKey(hmac.getBytes(), privateKey);
			String encryptMessage = encoder.encode(encodedData);
			log.info("encryptMessage msg "+encryptMessage);
			JSONObject resultJsonObject = JSONObject.parseObject(jsonString);;
			resultJsonObject.put("SIGNATURE", encryptMessage);
			String resultString = resultJsonObject.toString();
			log.info("sand xian data" +resultString);
			return resultString;
		} catch (UnsupportedEncodingException e) {
			log.error("HMACSHA256 encryptMessage error!",e);
		} catch (Exception e) {
			log.error("encryptMessage error...",e);
		}
		return "";
	}
	
	
	/**
	 * 格式化拼接传过来的字符
	 * @param inputString
	 * @return
	 */
	public static String formatInputJson(String inputString){
		StringBuffer sb = new StringBuffer();
		JSONObject json = null;
		try {
			json = JSONObject.parseObject(inputString);
			Set<String> set  = json.keySet();
			
			SortedMap<String, String> map = new TreeMap<String, String>();
			for(String key : set){
				if(null != json.getString(key) && !"".equals(json.getString(key)) && !"null".equals(json.getString(key))){
					map.put(key, json.getString(key));
				}
			}  
			for (Entry<String, String> string : map.entrySet()) {
				sb.append(string.getKey()+"=");
				sb.append(string.getValue()+"&");
			}
			String resultTemp = sb.toString();
			String result = resultTemp.substring(0,resultTemp.length()-1);
			log.info("encryptMessage :  "+result);
			return result;
		} catch (Exception e) {
			log.error("json error",e);
			log.error("encryptMessage fail",e);
		}
		return "";
	}
	
	/**
	 * HMACSHA256加密算法
	 * @param data
	 * @param key
	 * @return
	 */
	public static String HMACSHA256(byte[] data, byte[] key) 
	{
	      try  {
	         SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
	         Mac mac = Mac.getInstance("HmacSHA256");
	         mac.init(signingKey);
	         return byte2hex(mac.doFinal(data));
	      } catch (NoSuchAlgorithmException e) {
	         e.printStackTrace();
	      } catch (InvalidKeyException e) {
	        e.printStackTrace();
	      }
	      return null;
	} 
	public static String byte2hex(byte[] b){
	    StringBuilder hs = new StringBuilder();
	    String stmp;
	    for (int n = 0; b!=null && n < b.length; n++) {
	        stmp = Integer.toHexString(b[n] & 0XFF);
	        if (stmp.length() == 1) {
				hs.append('0');
			}
	        hs.append(stmp);
	    }
	    return hs.toString();
	}
	
	/**
	 * 校验传输的格式是否正确
	 * @return
	 */
	public static boolean formatCorrect(String ... args){
		boolean flag = true;
		for (String string : args) {
			if(stringIsNull(string)){
				return flag;
			}
		}
		flag = false;
		return flag;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean stringIsNull(String str){
		if(null == str || "".equals(str)){
			log.error("data is null...");
			return true;
		}
		return false;
	}
	
	
	/**
	 * 将byte[]转成utf-8格式的String
	 * 
	 * @param inputData
	 * @return
	 */
	public static String byteToString(byte[] inputData) {
		try {
			if (null == inputData) {
				return null;
			}
			return new String(inputData, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 处理返回消息
	 * @param privateKey
	 * @param returnMessage
	 * @return
	 */
	public static JSONObject handleReturnMessage(String secretkey,String privateKey,String returnMessage){
		JSONObject re = new JSONObject();
		try {
			if(null!=returnMessage || !"".equals(returnMessage)){
				JSONObject jsonObject = JSONObject.parseObject(returnMessage);
				String encryptMessageTemp = jsonObject.getString("SIGNATURE");
				String signature = decryptOpenFlatMessage(privateKey, encryptMessageTemp);
				//去除SIGNATURE进行加密校验
				jsonObject.remove("SIGNATURE");
				String formatString = formatInputJson(jsonObject.toString());
				String hmac = HMACSHA256(formatString.getBytes("UTF-8"),secretkey.getBytes("UTF-8"));
				log.info("receive encryptMessage "+hmac);
				//解密签名与明文json加密后的字符串对比
				if(hmac.equalsIgnoreCase(signature)){
					log.info("SIGNATURE success");
					re.put(MessageErrorCode.RESPONSE_MESSAGE_SUCCESS, jsonObject.toString());
					return re;
				}else{
					log.info("SIGNATURE fail");
					re.put(MessageErrorCode.RESPONSE_MESSAGE_ERROE, MessageErrorCode.RESPONSE_MESSAGE_DECRYPT_ERROR);
					return re;
				}
			}else{
				log.info("SIGNATURE fail");
				re.put(MessageErrorCode.RESPONSE_MESSAGE_ERROE, MessageErrorCode.RESPONSE_MESSAGE_DECRYPT_ERROR);
				return re;
			}
		} catch (Exception e) {
			log.error("SIGNATURE error",e);
			re.put(MessageErrorCode.RESPONSE_MESSAGE_ERROE, MessageErrorCode.RESPONSE_MESSAGE_FORMAT_ERROR);
			return re;
		}
	}

	/**
	 * 用私钥解密签名
	 * @param privateKey
	 * @param encryptMessage
	 * @return
	 */
	public static String decryptOpenFlatMessage(String privateKey, String encryptMessage){
		log.info("decryptOpenFlatMessage start...");
		if(FormatUtils.formatCorrect(privateKey,encryptMessage)){
			return MessageErrorCode.REQUEST_MESSAGE_FORMAT_ERROR;
		}
		String signatureStr="";
		try {
			signatureStr = FormatUtils.byteToString(RSAUtils.decryptByPrivateKey(decoder.decodeBuffer(encryptMessage) , privateKey));
			log.info("signatureStr is "+signatureStr);
		} catch (Exception e) {
			log.error("signature fail",e);
		}
		log.info("decryptOpenFlatMessage end...");
		return signatureStr;
	}
	
}
