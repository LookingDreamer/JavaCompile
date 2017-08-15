package com.sys.games.redPaper.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;


import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;


public class MoneyUtils {


	private static String appid ="wx89e8deb4c5d6b90a";// 应用ID
	private static String appsecret ="beafa63d953f6167c5e58074d81fcf79";// 应用密钥
	private static String partner ="1355314002";// 微信支付商户号
	private static String partnerkey ="e6MDVhCqABIzWVZv4glQDmJ97sf64XFZ";// 财付通初始密?
	private static String charset = "UTF-8";
	//final static String KEYSTORE_FILE =
	final static String KEYSTORE_PASSWORD = "1355314002";

	public static String randomNum(int num)
	{
		Random r=new Random();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<num;i++)
		{
			sb.append(r.nextInt(10));
		}
		return sb.toString();
	}

	public synchronized static String dateStr()
	{
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(new Date()).substring(12, 17);
	}

	/**
	 * 随机16为数值
	 * 
	 * @return
	 */
	public static String buildRandom() {
		String currTime = TenpayUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < 4; i++) {
			num = num * 10;
		}
		return (int) ((random * num)) + strTime;
	}

	public static String defaultRandom()
	{
		return randomNum(4)+dateStr()+randomNum(3);
	}


	public static void main(String args[]){
//		String currTime = TenpayUtil.getCurrTime();
//		String strTime = currTime.substring(8, currTime.length());
//		System.out.println(currTime);
//		System.out.println(strTime);
//		System.out.println(buildRandom());
//		System.out.println(randomNum(4));
//		System.out.println(randomNum(8));
//		System.out.println(randomNum(10));
//		System.out.println(randomNum(16));
		System.out.println(defaultRandom());
		System.out.println(defaultRandom().length());
	}



	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 sign
	 */
	public static String createSign(Map<String, Object> map) {
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		for (Map.Entry<String, Object> m : map.entrySet()) {
			packageParams.put(m.getKey(), m.getValue().toString());
		}

		StringBuffer sb = new StringBuffer();
		Set<?> es = packageParams.entrySet();
		Iterator<?> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!StringUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + partnerkey);
		 
		
		String sign = MD5Util.MD5Encode(sb.toString(), charset).toUpperCase();
		return sign;
	}

	public static String getOrderNo() {
		String order = partner
				+ new SimpleDateFormat("yyyyMMdd").format(new Date());
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			order += r.nextInt(9);
		}
		return order;
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(getOrderNo()); }
	 */
	public static boolean doSendMoney(String url,Map<String, Object> param)
	{
		try {
			String out = doSendMoney(url, createXML(param));
			LogUtil.info("发送红包结果，result:" + out);
			return out.contains("发放成功");
		}
		catch (Exception ex)
		{
			LogUtil.error(ex);
			return false;
		}
	}


	public static String doSendMoney2(String url,Map<String, Object> param)
	{
		try {
			String out = doSendMoney(url, createXML(param));
			LogUtil.info("发送红包结果，result:" + out);
			return out;
		}
		catch (Exception ex)
		{
			LogUtil.error(ex);
			return "false";
		}
	}


	public static String doSendMoney(String url, String data) throws Exception {
		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
		String KEYSTORE_FILE=ValidateUtil.getConfigValue("wx.sentRedPaper.cert");
		LogUtil.info("红包-KEYSTORE_FILE:"+KEYSTORE_FILE);
		 FileInputStream instream = new FileInputStream(new File(KEYSTORE_FILE));//P12文件目录


		//InputStream instream = MoneyUtils.class.getResourceAsStream("/apiclient_cert.p12");
        try {
            keyStore.load(instream, KEYSTORE_PASSWORD.toCharArray());//这里写密码..默认是你的MCHID
        }
		catch (Exception ex)
		{
			LogUtil.error("发送红包keyStore加载失败");
			LogUtil.error(ex);
			return "";
		}
		finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, KEYSTORE_PASSWORD.toCharArray())//这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
        		SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
        	HttpPost httpost = new HttpPost(url); // 设置响应头信息
        	httpost.addHeader("Connection", "keep-alive");
        	httpost.addHeader("Accept", "*/*");
        	httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        	httpost.addHeader("Host", "api.mch.weixin.qq.com");
        	httpost.addHeader("X-Requested-With", "XMLHttpRequest");
        	httpost.addHeader("Cache-Control", "max-age=0");
        	httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
    		httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = toStringInfo(response.getEntity(),"UTF-8");
                
                //微信返回的报文时GBK，直接使用httpcore解析乱码
              //  String jsonStr = EntityUtils.toString(response.getEntity(),"UTF-8");
                EntityUtils.consume(entity);
               return jsonStr;
            }
			catch (Exception ex)
			{
				LogUtil.error("发送红包，post给腾讯，获取返回的内容出现异常");
				LogUtil.error(ex);
				return "";
			}finally {
                response.close();
            }
        }
		catch (Exception ex)
		{
			LogUtil.error("发送红包,post给腾讯失败");
			LogUtil.error(ex);
			return "";
		}
		finally {
            httpclient.close();
        }
	}
	
	private static String toStringInfo(HttpEntity entity, String defaultCharset) throws Exception, IOException{
		final InputStream instream = entity.getContent();
	    if (instream == null) {
	        return null;
	    }
	    try {
	        Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
	                "HTTP entity too large to be buffered in memory");
	        int i = (int)entity.getContentLength();
	        if (i < 0) {
	            i = 4096;
	        }
	        Charset charset = null;
	        
	        if (charset == null) {
	            charset = Charset.forName(defaultCharset);
	        }
	        if (charset == null) {
	            charset = HTTP.DEF_CONTENT_CHARSET;
	        }
	        final Reader reader = new InputStreamReader(instream, charset);
	        final CharArrayBuffer buffer = new CharArrayBuffer(i);
	        final char[] tmp = new char[1024];
	        int l;
	        while((l = reader.read(tmp)) != -1) {
	            buffer.append(tmp, 0, l);
	        }
	        return buffer.toString();
	    } finally {
	        instream.close();
	    }
	}
	
	public static String createXML(Map<String, Object> map){
		String xml = "<xml>";
		Set<String> set = map.keySet();
		Iterator<String> i = set.iterator();
		while(i.hasNext()){
			String str = i.next();
			xml+="<"+str+">"+"<![CDATA["+map.get(str)+"]]>"+"</"+str+">";
		}
		xml+="</xml>";
		return xml;
	}
}
