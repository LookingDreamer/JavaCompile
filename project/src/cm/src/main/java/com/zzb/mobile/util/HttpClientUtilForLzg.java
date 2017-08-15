package com.zzb.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class HttpClientUtilForLzg {
	public static boolean DEBUG = false;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Map<String, Map<String, String>> configMap = new HashMap<String, Map<String, String>>();
	private static Map<String, HttpClient> clientMap = new ConcurrentHashMap<String, HttpClient>();
	public static String USER_AGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
	public static String CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static String ACCEPT_LANGUAGE = "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3";
	
	
	 
	public static HttpClient getClientByInsuId(String insuId) {
		if(clientMap.containsKey(insuId))
			return clientMap.get(insuId);
		else 
			return new DefaultHttpClient();
	}

	public static Map<String, HttpClient> getClientMap() {
		return clientMap;
	}

	public static Map<String, String> getConfigMap(String key) {
		return configMap.get(key);
	}

	public static Set<String> getConfigMapKeySet() {
		return configMap.keySet();
	}

	public static void putConfigMap(String insuId, Map<String, String> map) {
		configMap.put(insuId, map);
	}

	public static String replaceELExp(String exp, String value, String string) {
//		value = value.replaceAll("%", "%25");
//		value = value.replaceAll("\\+", "%2B");
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
		}
		Matcher matcher = Pattern.compile("(?<=\\$\\{) *" + exp + " *?(?=\\})").matcher(string);
		while (matcher.find()) {
			string = string.replace("${" + matcher.group() + "}", value);
		}
		return string;
	}

	public static String getContent(HttpResponse response, String encode) throws IllegalStateException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.getEntity().writeTo(baos);
		String bosStr = baos.toString(encode == null ? "GBK" : encode);
		return bosStr;
	}

	public static String getExStackTrac(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(baos));
		return baos.toString();
	}

	public static HttpGet getHttpGet(String url) {
		HttpGet get = new HttpGet(url);
		get.setHeader("Content-Type", CONTENT_TYPE);
		get.setHeader("User-Agent", USER_AGENT);
		get.setHeader("Accept-Language", ACCEPT_LANGUAGE);
		return get;
	}

	public static HttpPost getHttpPost(String url) {
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", CONTENT_TYPE);
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Accept-Language", ACCEPT_LANGUAGE);
		return post;
	}

	public static String transMACAddr(String macaddr, String splitChar){
		return macaddr.substring(0, 2) + splitChar + macaddr.substring(2, 4) + splitChar
				+ macaddr.substring(4, 6) + splitChar + macaddr.substring(6, 8) + splitChar + macaddr.substring(8, 10)
				+ splitChar + macaddr.substring(10, 12);
	}

	public static String executeGetRequest(HttpClient httpClient, String url, Map<String, String> paramsMap, String enc){
		String entityStr = null;
		String queryString = "";
		for(String key : paramsMap.keySet()){
			if("queryString".equals(key))
				queryString = paramsMap.get(key);
			else
				queryString += "&" + key + "=" + paramsMap.get(key);
		}
		if(url.indexOf("?") > 0){
			if(url.endsWith("?")){
				url += queryString.substring(1);
			}else{
				url += queryString;
			}
		}else{
			url += "?" + (queryString.startsWith("&") ? queryString.substring(1) : queryString);
		}
		System.out.println(url);
		try {
			HttpGet get = getHttpGet(url);
			HttpResponse response = httpClient.execute(get);
			return getContent(response, enc);
		} catch (IOException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			e.printStackTrace();
			entityStr = baos.toString();
		}
		return entityStr;
	}
	
	public static String executePostRequest(HttpClient httpClient, String url, Map<String, String> paramsMap, String enc){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		HttpPost post = getHttpPost(url);
		String entityStr = null;
		for(String key : paramsMap.keySet()){
			if(key.equals("queryString")){
				entityStr = paramsMap.get(key);
			}else{
				list.add(new BasicNameValuePair(key, paramsMap.get(key)));
			}
		}
		HttpEntity entity;
		try {
			if(entityStr != null){
				entity = new StringEntity(entityStr);
			}else{
				entity = new UrlEncodedFormEntity(list, enc == null ? "GBK" : enc);
			}
//			HttpClient httpClient = new DefaultHttpClient();
//			addSSL2Client(httpClient);
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			return getContent(response, enc);
		} catch (IOException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			e.printStackTrace();
			entityStr = baos.toString();
		}
		return entityStr;
	}

	public static void addSSL2Client(HttpClient client) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
				public X509Certificate[] getAcceptedIssuers() { return null; }
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
			client = new DefaultHttpClient(ccm, client.getParams());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String post(String url, Map<String, String> map){
		long s = System.currentTimeMillis();
		HttpClient client = new DefaultHttpClient();
		String result =  executePostRequest(client, url, map, "UTF-8");
		long e = System.currentTimeMillis();
//		System.out.println(sdf.format(new Date()) + " " + url + " " + map.get("openId") + "  耗时 " + (e - s) + " ms");
		if(result.indexOf("errorCode\":\"-1\"") > 0){
			System.out.println(sdf.format(new Date()) + " " + url + " " + map.get("openId") + " 耗时 " + (e - s) + " ms\n"
					+ map.toString() + "\n" + result);
		}
		return result;
	}
	public static String get(String url, Map<String, String> map){
		long s = System.currentTimeMillis();
		HttpClient client = new DefaultHttpClient();
		String result =  executeGetRequest(client, url, map, "UTF-8");
		long e = System.currentTimeMillis();
//		System.out.println(sdf.format(new Date()) + " " + url + " " + map.get("openId") + " 耗时 " + (e - s) + " ms");
		if(result.indexOf("errorCode\":\"-1\"") > 0){
			System.out.println(sdf.format(new Date()) + " " + url + " " + map.get("openId") + " 耗时 " + (e - s) + " ms\n"
					+ map.toString() + "\n" + result);
		}
		return result;
	}
	
    
	public static String httpPostWithJSON(String url, String json) {
		String entityStr = null;
		try {
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        StringEntity entity = new StringEntity(json, "UTF-8");  
	        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded"));//"application/octet-stream"  
	        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));  	          
	        HttpPost httpPost = new HttpPost(url);  
	        httpPost.setHeader("Accept", "application/json");  
	        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");  
	        httpPost.setHeader("Accept-Language", ACCEPT_LANGUAGE);
	        httpPost.setEntity(entity);  
	        
	        HttpResponse response = httpClient.execute(httpPost);
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != HttpStatus.SC_OK){
                System.out.println("Method failed:"+response.getStatusLine());
            }
	        return getContent(response, "UTF-8");
		} catch (IOException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			e.printStackTrace();
			entityStr = baos.toString();
		}
		return entityStr;
    }
}
