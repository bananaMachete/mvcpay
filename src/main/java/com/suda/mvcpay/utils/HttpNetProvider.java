package com.suda.mvcpay.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author tim
 */
public class HttpNetProvider {

	private static Log log  = LogFactory.getLog(HttpNetProvider.class);

	public static byte[] doGetBytes(String url){
		InputStream is=null;
		ByteArrayOutputStream os=null;
		try {
			URL httpUrl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();
			conn.setDoInput(true);
			is=conn.getInputStream();
			os = new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			int length=-1;
			while((length=is.read(b, 0, 1024))!=-1){
				os.write(b,0,length);
			}
			conn.disconnect();
			return os.toByteArray();
		} catch (MalformedURLException e) {
			log.error("MalformedURLException",e);
		} catch (IOException e) {
			log.error("IOException",e);
		}finally{
			if(os!=null) {
				try {
					os.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
		}
		return null;
	}
	
	//调用自己接口的方法，例如从微信银行调用企业号
	public static byte[] doPostBytes(String url,byte[] data){
		InputStream is=null;
		OutputStream os=null;
		ByteArrayOutputStream bos=null;
		try {
			log.info("do post url:"+url);
			log.info("do post data:"+new String(data,"utf-8"));
			URL httpUrl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();	
			conn.setConnectTimeout(15*1000);
			conn.setReadTimeout(5*1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			os=conn.getOutputStream();
			os.write(data);
			os.flush();
			is=conn.getInputStream();
			bos = new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			int length=-1;
			while((length=is.read(b, 0, 1024))!=-1){
				bos.write(b,0,length);
			}
			conn.disconnect();
			if((new String(bos.toByteArray(),"utf-8")).length() <=3000){
				log.info("do post return:"+new String(bos.toByteArray(),"utf-8"));
			}else {
				log.info("--------回应数据长度超过3000，日志级别修改为debug--------");
				log.debug("do post return:"+ new String(bos.toByteArray(),"utf-8"));
			}
			return bos.toByteArray();
		} catch (MalformedURLException e) {
			log.error("MalformedURLException",e);
		} catch (IOException e) {
			//logger.error(e.getMessage());
			log.error("IOException",e);
		}catch (Exception e) {
			log.error("Exception",e);
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					log.error("IOException",e);
				}
			}
			if(bos!=null) {
				try {
					bos.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
		}
		return null;
	}
	
	//调用第三方接口的方法
	public static byte[] doPostBytes(String url,byte[] data,String requestContentType){
		InputStream is=null;
		OutputStream os=null;
		ByteArrayOutputStream bos=null;
		try {
			URL httpUrl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();		
			conn.setRequestProperty("content-type", requestContentType);
			conn.setReadTimeout(20*1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			os=conn.getOutputStream();
			os.write(data);
			os.flush();
			is=conn.getInputStream();
			bos = new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			int length=-1;
			while((length=is.read(b, 0, 1024))!=-1){
				bos.write(b,0,length);
			}
			conn.disconnect();
			log.info("do post return:"+new String(bos.toByteArray(),"utf-8"));
			return bos.toByteArray();
		} catch (MalformedURLException e) {
			log.error("MalformedURLException",e);
		} catch (IOException e) {
			log.error("IOException",e);
		}catch (Exception e) {
			log.error("Exception",e);
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					log.error("IOException",e);
				}
			}
			if(bos!=null) {
				try {
					bos.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
		}
		return null;
	}
	
	//调用自己接口的方法
	public static byte[] doPostBytes(String url,String data){
		try {
			return doPostBytes(url,data.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException",e);
		} 
		return null;
	}
	
	public static byte[] doPostBytess(String url,String data){
		try {
			return doPostBytess(url,data.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException",e);
		} catch (Exception e) {
			log.error("Exception",e);
		}
		return null;
	}
	
	//调用第三方接口的方法
	public static byte[] doPostBytes(String url,String data,String requestContentType){
		try {
			return doPostBytes(url,data.getBytes("utf-8"),requestContentType);
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException",e);
		}
		return null;
	}
	
	
	public static byte[] doPostBytess(String url,byte[] data){
		InputStream is=null;
		OutputStream os=null;
		ByteArrayOutputStream bos=null;
		try {
			URL httpUrl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();	
			conn.setConnectTimeout(2*1000);
			conn.setReadTimeout(3*1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			os=conn.getOutputStream();
			os.write(data);
			os.flush();
			is=conn.getInputStream();
			bos = new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			int length=-1;
			while((length=is.read(b, 0, 1024))!=-1){
				bos.write(b,0,length);
			}
			conn.disconnect();
			return bos.toByteArray();
		} catch (MalformedURLException e) {
			log.error("MalformedURLException",e);
		} catch (IOException e) {
			//logger.error(e.getMessage());
			log.error("IOException",e);
		}catch (Exception e) {
			log.error("Exception",e);
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					log.error("IOException",e);
				}
			}
			if(bos!=null) {
				try {
					bos.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("IOException", e);
				}
			}
		}
		return null;
	}
	
	/**
     * POST请求获取数据
     */
    public static String postJson(String path,String post){
		log.info("POST请求数据:" + post);
        URL url = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        String retStr = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(post);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            bis = new BufferedInputStream(httpURLConnection.getInputStream());
            bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            httpURLConnection.disconnect();
            retStr = bos.toString("utf-8");
			log.info("POST返回数据：" + retStr);
        } catch (Exception e) {
			log.info("Exception",e);
        } finally {
        	if(null != bos){
        		try {
        			bos.close();
        		} catch (IOException e) {
					log.error("IOException",e);
        		}
        	}
        	if(null != bis){
        		try {
        			bis.close();
        		} catch (IOException e) {
					log.error("IOException",e);
        		}
        	}
        }
        return retStr;
    }
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		int persons = 6000;
		byte[] b = doGetBytes("http://hq.sinajs.cn/list=s_sh000001");
		BigDecimal sh = new BigDecimal(new String(b,"gbk").split(",")[1]);//上证指数
		b = doGetBytes("http://hq.sinajs.cn/list=s_sz399001");
		BigDecimal sz = new BigDecimal(new String(b,"gbk").split(",")[1]);//深证成指
		b = doGetBytes("http://hq.sinajs.cn/list=int_hangseng");
		BigDecimal hs = new BigDecimal(new String(b,"gbk").split(",")[1]);//恒生指数
		String zs = sh.multiply(sz).multiply(hs).toString().split("\\.")[0];//上证指数*深证成指*恒生指数，取整数
		BigDecimal dx = new BigDecimal(new StringBuffer(zs).reverse().toString());//倒序
		BigDecimal luckynum = dx.divideAndRemainder(new BigDecimal(persons))[1].add(new BigDecimal(1));//除以人数取模+1
		System.out.println(luckynum);
	}
}
