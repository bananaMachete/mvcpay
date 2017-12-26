package com.suda.mvcpay.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropsUtil {
	private static Log log  = LogFactory.getLog(PropsUtil.class);

	private static Properties props = new Properties();
	static{
		InputStream in = PropsUtil.class.getClass().getResourceAsStream("/resources/urlConfig.properties");
		try {
			props.load(in);
		} catch (IOException e) {
			log.error("IOException:",e);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				log.error("IOException:",e);
			}
		}
	}
	
	public static String getProperty(String key){
		return props.getProperty(key);
	}
}
