package com.zzb.mobile.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClient {
	/**
	 * 调用支付平台接口
	 */
	private static final CloseableHttpClient httpClient;
	private static final String CHARSET = "UTF-8";
	static {
		Integer connectTimeout =com.common.ConfigUtil.getPropInteger("http.connecttimeout",100000);
		Integer sockettimeout =com.common.ConfigUtil.getPropInteger("http.sockettimeout",20000);
		Integer maxconntotal =com.common.ConfigUtil.getPropInteger("http.maxconntotal",400);
		Integer maxconnperroute =com.common.ConfigUtil.getPropInteger("http.maxconnperroute",200);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout)
				.setSocketTimeout(sockettimeout).build();
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setMaxConnTotal(maxconntotal);
		clientBuilder.setMaxConnPerRoute(maxconnperroute);
		clientBuilder.setDefaultRequestConfig(config);
		httpClient = clientBuilder.build();
	}
	/**
	 * 调用支付平台接口支付
	 * @param bizId -业务主键
	 * @param url -接口地址
	 * @param parameters -支付参数
	 * @return
	 */
	public static String sendPostJson(String bizId,String url, JSONObject parameters) {
		return sendPost(bizId, url, parameters.toString());
	}
	/**
	 * 调用支付平台接口支付
	 * @param bizId -业务主键
	 * @param url -接口地址
	 * @param parameters -支付参数
	 * @return
	 */
	public static String sendPost(String bizId,String url, String parameters) {
		String resultStr = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		OutputStream out = null;
		StringBuffer sb = new StringBuffer();// 处理请求参数
		try {
			if(url.contains("${bizId}")){
				url=url.replace("${bizId}", bizId);
			}
			HttpPost httpPost = new HttpPost(url);
			System.out.println("parameters is ----"+parameters);
			StringEntity stringEntity = new StringEntity(parameters,ContentType.create("application/json","utf-8"));       
            httpPost.setEntity(stringEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort(); 
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				resultStr = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			
		}catch(Exception e){
			resultStr = e.getMessage();
			e.printStackTrace();
		}
		return resultStr;	
	}

	public static String sendPostV2(String url, String parameters) {
		String resultStr = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		OutputStream out = null;
		StringBuffer sb = new StringBuffer();// 处理请求参数
		try {
			HttpPost httpPost = new HttpPost(url);
			System.out.println("parameters is ----"+parameters);
			StringEntity stringEntity = new StringEntity(parameters,ContentType.create("application/json","utf-8"));
			httpPost.setEntity(stringEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				resultStr = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();

		}catch(Exception e){
			resultStr = e.getMessage();
			e.printStackTrace();
		}
		return resultStr;
	}


	public static String doPayGet(String url, Map<String, String> params){
		String result = null;
		try {
			if(url.contains("${bizId}")){
				url=url.replace("${bizId}", params.get("bizId"));
			}
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				result = ("HttpClient,error status code :"+ statusCode + response.getStatusLine());
				System.out.println("result is " + response.getStatusLine());
			}
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 调用工作流接口
	 * @throws IOException 
	 */
	public static String doWorkFlowGet(String url, String params){
		try {
			if (params != null && !params.isEmpty()) {
				url =url+"?"+params;
			}
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :"
						+ statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args){
		try {
			String s ="{\"channelId\":\"bestpay\",\"channelName\":\"翼支付\",\"payType\":\"mobile\","
					+ "\"payTypeDesc\":\"移动支付\",\"bizId\":\"zzb20151104000000003\","
					+ "\"paySource\":\"zzb.system.core\",\"amount\":\"1\",\"notifyUrl\":\"http://203.195.141.57:8080/cm/mobile/login/pay/callback\","
					+ "\"notifyType\":\"POST\",\"areaCode\":\"11\",\"agentOrg\":\"1244000000\",\"insOrg\":\"111\","
					+ "\"channelParam\":{\"bankCardNo\":\"62226009100449979201\","
					+ "\"bankCardMobile\":\"13811575109\",\"bankArea\":\"110100\",\"idCardName\":\"陈玉章\","
					+ "\"idCardNo\":\"320324197903150339\",\"idCardType\":\"1\",\"idCardAddress\":\"江苏省徐州市睢宁县石土庙村\"},"
					+ "\"sign\":\"D209252E135412E1D4BB6B365A9A9ECF\"}";
			HttpClient.sendPost("zzb20151104000000003", "http://cninsure-2011.gicp.net:33669/PMService/restful/paymentService/zzb20151104000000003/pay", s);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
