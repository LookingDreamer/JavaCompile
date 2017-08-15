/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.chetong.sdk.enums.ChetongErrorEnum;
import net.chetong.sdk.exception.ChetongApiException;

/**
 * HTTP(HTTPS)请求发送工具类
 * 
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月29日 下午3:29:05
 * @since JDK 1.7
 */
public class HttpClientUtils {
   
	/**
	 * 发送GET请求
	 * @author 2016年6月29日  下午3:31:03  温德彬
	 * @param url 请求地址
	 * @param params 参数
	 * @param connectTimeout 连接超时时间
	 * @param readTimeout 回应超时时间
	 * @return
	 */
	public static String sendGet(String url,Map<String,Object> params,int connectTimeout, int readTimeout) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + buildQuery(params, "UTF-8").substring(1);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！");
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 发送POST请求
	 * @author 2016年6月29日  下午3:32:02  温德彬
	 * @param url 请求地址
	 * @param params 参数
	 * @param connectTimeout 连接超时时间
	 * @param readTimeout 回应超时时间
	 * @return
	 */
	public static String sendPost(String url,Map<String,Object> params,int connectTimeout, int readTimeout){
		OutputStream out = null;
		BufferedReader in = null;
		String result = "";
		HttpURLConnection conn = null;
		HttpsURLConnection connHttps;
		try {
			URL realUrl = new URL(url);
			if (realUrl.getProtocol().equals("https")) {
				SSLContext ctx = null;
				try {
					ctx = SSLContext.getInstance("TLS");
					ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() },
							new SecureRandom());
				} catch (Exception e) {
					throw new IOException(e);
				}
				connHttps = (HttpsURLConnection) realUrl.openConnection();
				connHttps.setSSLSocketFactory(ctx.getSocketFactory());
				connHttps.setHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});
				conn = connHttps;
			}else{
				conn = (HttpURLConnection) realUrl.openConnection();
			}
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "application/json, text/plain, */*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "UTF-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.connect();
			conn.setConnectTimeout(connectTimeout);
		    conn.setReadTimeout(readTimeout);

			// 获取URLConnection对象对应的输出流
			out = conn.getOutputStream();
			// 发送请求参数
			String param = buildQuery(params,"UTF-8");
			if (param != null) {
				out.write(param.getBytes("UTF-8"));
			}
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 构建参数
	 * @author 2016年6月29日  下午3:36:43  温德彬
	 * @param params 参数
	 * @param charset 字符类型
	 * @return
	 * @throws IOException
	 */
	public static String buildQuery(Map<String, Object> params, String charset) throws IOException {
	    if ((params == null) || (params.isEmpty())) {
	      return null;
	    }
	    StringBuilder query = new StringBuilder();
	    for (Entry<String, Object> entry:params.entrySet()) {
	      String name = (String)entry.getKey();
	      String value = (String)entry.getValue();
	      if (!StringUtil.isNullOrEmpty(name)) {
	        query.append("&");
	        query.append(name).append("=").append(URLEncoder.encode(value, charset));
	      }
	    }

	    return query.toString();
	  }
	
	/**
	 * HTTPS信任管理
	 * @version 1.0
	 * @author 温德彬
	 * @time 2016年6月29日  下午3:33:07
	 * @since JDK 1.7
	 */
	private static class DefaultTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	}
	
}
