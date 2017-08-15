package com.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.FileCopyUtils;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.SpringContextHandle;
import com.cninsure.core.utils.StringUtil;

public class HttpClientUtil {
	private static final HttpClientBuilder clientBuilder;
	private static HttpHost mProxyHost ;
	private static Credentials mCredentials;
	public static final String CHARSET = "UTF-8";

	static {
		Integer connectTimeout =com.common.ConfigUtil.getPropInteger("http.connecttimeout",30000);
		Integer sockettimeout =com.common.ConfigUtil.getPropInteger("http.sockettimeout",20000);
		Integer maxconntotal =com.common.ConfigUtil.getPropInteger("http.maxconntotal",400);
		Integer maxconnperroute =com.common.ConfigUtil.getPropInteger("http.maxconnperroute",200);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(sockettimeout).build();
	    clientBuilder = HttpClientBuilder.create();
		clientBuilder.setMaxConnTotal(maxconntotal);
		clientBuilder.setMaxConnPerRoute(maxconnperroute);
		clientBuilder.setDefaultRequestConfig(config);
	}
	
	private static ThreadPoolTaskExecutor taskthreadPool4workflow;

	private static CloseableHttpClient createClient() {
		return clientBuilder.build();
	}

	private static void closeClient(CloseableHttpClient client) {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String doGet(String url, Map<String, String> params) {
		if (url != null && (url.contains("/process/completeSubTask") || url.contains("/process/completeTask") ||
				url.contains("/process/abortProcessById") || url.contains("/process/setProcessSuccess") ||
				url.contains("/process/completeLudanAndWriteBack"))) {
			WorkflowAttachHandleUtil.record("GET", url, params);
			JSONObject json = JSONObject.fromObject(params.get("datas"));
			if(url.contains("/process/completeSubTask") || url.contains("/process/completeLudanAndWriteBack")){//完成子任务处理cm业务数据逻辑
				WorkFlowUtil.completeSubTaskDealCmData(String.valueOf(json.get("processinstanceid")), String.valueOf(json.get("taskName")), "Completed");
			}else if(url.contains("/process/completeTask")){//完成主任务处理cm业务数据逻辑
				WorkFlowUtil.completeMainTaskDealCmData(String.valueOf(json.get("processinstanceid")), String.valueOf(json.get("taskName")), "Completed");
			}else if(url.contains("/process/abortProcessById")){//取消任务处理cm业务数据逻辑
				WorkFlowUtil.abortTaskDealCmData(String.valueOf(json.get("processinstanceid")), String.valueOf(json.get("mainprocessinstanceid")), String.valueOf(json.get("process")), String.valueOf(json.get("from")));
			}
		}
		if(url.contains("/process/completeSubTask") || url.contains("/process/completeLudanAndWriteBack")
				||url.contains("/process/completeTask") || url.contains("/process/abortProcessById")){
			if(null == taskthreadPool4workflow){
				taskthreadPool4workflow = (ThreadPoolTaskExecutor)SpringContextHandle.getBean(ThreadPoolTaskExecutor.class);
			}
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					doGet(url, params, CHARSET);
				}
			});
			return "{'message':'success'}";
		}else{
			return doGet(url, params, CHARSET);
		}
		
	}

	public static String doTaskGet(String url, Map<String, String> params)
			throws ParseException, UnsupportedEncodingException, IOException {
		return doTaskGet(url, params, CHARSET);
	}

	public static String doPost(String url, Map<String, String> params) throws ClientProtocolException, IOException {
		return doPost(url, params, CHARSET);
	}

	public static String doPostJson(String url, Map<String, String> params)
			throws ClientProtocolException, IOException {
		return doPostJson(url, params, CHARSET);
	}
	
	

	/**
	 * HTTP Get 获取内容
	 * 
	 * @param url
	 *             请求的url地址
	 * @param params
	 *            请求的参数
	 * @param charset
	 *            编码格式
	 * @return
	 */
	public static String doGet(String url, Map<String, String> params, String charset) {
		LogUtil.info("GET:url=" + url + ", params=" + params);
		CloseableHttpClient httpClient = createClient();
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
			}
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			LogUtil.info("GET:url=" + url + ", result=" + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeClient(httpClient);
		}
		return null;
	}

	public static String doTaskGet(String url, Map<String, String> params, String charset)
			throws ParseException, UnsupportedEncodingException, IOException {
		LogUtil.info("GET:url=" + url + ", params=" + params);
		CloseableHttpClient httpClient = createClient();
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
			}
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			LogUtil.info("GET:url=" + url + ", result=" + result);
			return result;
		} finally {
			closeClient(httpClient);
		}
	}

	/**
	 * HTTP Post 获取内容
	 * 
	 * @param url
	 *            请求的url地址
	 * @param params
	 *            请求的参数
	 * @param charset
	 *            编码格式
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPost(String url, Map<String, String> params, String charset)
			throws ClientProtocolException, IOException {
		LogUtil.info("POST:url=" + url + ", params=" + params);
		CloseableHttpClient httpClient = createClient();
		try {
			List<NameValuePair> pairs = null;
			if (params != null && !params.isEmpty()) {
				pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			if (pairs != null && pairs.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
			}
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			LogUtil.info("POST:url=" + url + ", result=" + result);
			return result;
		} finally {
			closeClient(httpClient);
		}
	}

	/**
	 * 直接传递Json对象
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPostJson(String url, Map<String, String> params, String charset)
			throws ClientProtocolException, IOException {
		LogUtil.info("POS:url=" + url + ", params=" + params);
		CloseableHttpClient httpClient = createClient();
		try {
			JSONObject jsonObject = new JSONObject();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						jsonObject.put(entry.getKey(), value);
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			LogUtil.info("POST:url=" + url + ", result=" + result);
			return result;
		} finally {
			closeClient(httpClient);
		}
	}

	/**
	 * 直接传递json String
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostJsonString(String url, String json) {
		LogUtil.info("POST:url=" + url + ", params=" + json);
		CloseableHttpClient httpClient = createClient();
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(json, "utf-8");
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			LogUtil.info("POST:url=" + url + ", result=" + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeClient(httpClient);
		}
		return null;
	}

	/**
	 * 传递字节
	 * 
	 * @param url
	 * @param content
	 * @return
	 */
	public static String doPostBytes(String url, byte[] content) {
		LogUtil.info("POST:url=" + url);
		String result = null;
		CloseableHttpClient httpClient = createClient();
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(EntityBuilder.create().setBinary(content).build());
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeClient(httpClient);
		}
		LogUtil.info("POST:url=" + url + ", result=" + result);
		return result;
	}

	/**
	 * 直接传递Json对象
	 * 
	 * @param url
	 * @param params
	 * @param connectTimeout
	 *            连接超时时间（毫秒）
	 * @param socketTimeout
	 *            响应超时时间（毫秒）
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPostJsonWithTimeout(String url, Map<String, String> params, Integer connectTimeout,
			Integer socketTimeout) {
		LogUtil.info("POST:url=" + url + ", params=" + params);
		CloseableHttpClient httpClient = createClient();
        String result = null;
		try {
			JSONObject jsonObject = new JSONObject();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						jsonObject.put(entry.getKey(), value);
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			CloseableHttpResponse response = null;
			if (!StringUtil.isEmpty(connectTimeout) || !StringUtil.isEmpty(socketTimeout)) {
				RequestConfig config = RequestConfig.custom()
						.setConnectTimeout(StringUtil.isEmpty(connectTimeout) ? 100000 : connectTimeout)
						.setSocketTimeout(StringUtil.isEmpty(socketTimeout) ? 20000 : socketTimeout).build();
				CloseableHttpClient httpClient1 = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
				response = httpClient1.execute(httpPost);
			} else
				response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
		}catch (Exception e){
            e.printStackTrace();
        }finally {
			closeClient(httpClient);
		}
        LogUtil.info("POST:url=" + url + ", result=" + result);
        return result;
	}

	/**
	 * 下载文件
	 * 
	 * @param downloadUrl
	 * @param params
	 * @param filepath
	 * @param filename
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static String downloadFile(String downloadUrl, Map<String, String> params, String filepath, String filename)
			throws ParseException, UnsupportedEncodingException, IOException {
		return downloadFile(downloadUrl, params, filepath, filename, CHARSET);
	}

	/**
	 * 下载文件
	 * 
	 * @param downloadUrl
	 * @param params
	 * @param filepath
	 * @param filename
	 * @param charset
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static String downloadFile(String downloadUrl, Map<String, String> params, String filepath, String filename,
			String charset) throws ParseException, UnsupportedEncodingException, IOException {
		CloseableHttpClient httpClient = createClient();
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				file.mkdirs();
			}
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				downloadUrl += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
				LogUtil.info("http url 下载图片, url = " + downloadUrl);
			}
			HttpGet httpGet = new HttpGet(downloadUrl);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			file = new File(filepath + "/" + filename);
			FileOutputStream fos = new FileOutputStream(file);
			FileCopyUtils.copy(is, fos);
			return filepath + "/" + filename;
		} finally {
			closeClient(httpClient);
		}
	}
	
	
	private static CloseableHttpAsyncClient createHttpClient() {
        final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectionRequestTimeout(1000);
        requestConfigBuilder.setConnectTimeout(20000);
        requestConfigBuilder.setSocketTimeout(30000);
        if (mProxyHost != null) {
            requestConfigBuilder.setProxy(mProxyHost);
        }
        final HttpAsyncClientBuilder clientBuilder = HttpAsyncClients.custom();
        final RequestConfig requestConfig = requestConfigBuilder.build();
        clientBuilder.setDefaultRequestConfig(requestConfig);
        if (mProxyHost != null && mCredentials != null) {
            final AuthScope authScope = new AuthScope(mProxyHost);
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(authScope, mCredentials);
            clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            clientBuilder.setMaxConnPerRoute(20);
            clientBuilder.setMaxConnTotal(200);
        }
        return clientBuilder.build();
    }
	
	/**
	 * 异步post 请求  参数 params
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String doPostAsyncClient(String url, Map<String, String> params, String charset)
			throws  IOException {
		 CloseableHttpAsyncClient httpclient =  createHttpClient();
		 httpclient.start();
		try {
			List<NameValuePair> pairs = null;
			if (params != null && !params.isEmpty()) {
				pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			if (pairs != null && pairs.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
			}
			Future<HttpResponse> future = httpclient.execute(httpPost,null);
			HttpResponse response = future.get();
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.close();
		}
		return null;
	}
	
	/**
	 * 异步post 请求  参数 params json 模式
	 * @param url
	 * @param json
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String doPostJsonAsyncClientJson(String url, String json,String charset){
		 CloseableHttpAsyncClient httpclient =  createHttpClient();
		 httpclient.start();
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(json, "utf-8");
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			Future<HttpResponse> future = httpclient.execute(httpPost,null);
			HttpResponse response = future.get();
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 异步post 请求  参数 params json 模式
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String doPostJsonAsyncClient(String url, Map<String, String> params, String charset) throws IOException {
		try {
			JSONObject jsonObject = new JSONObject();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						jsonObject.put(entry.getKey(), value);
					}
				}
			}
			return doPostJsonAsyncClientJson(url, jsonObject.toString(),charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 异步get 请求  参数 params
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String doGetAsyncClient(String url, Map<String, String> params, String charset) throws IOException {
		 CloseableHttpAsyncClient httpclient =  createHttpClient();
		 httpclient.start();
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
			}
			HttpGet httpGet = new HttpGet(url);
			Future<HttpResponse> future = httpclient.execute(httpGet,null);
			HttpResponse response = future.get();// 获取结果
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.close();
		}
		return null;
	}

	public static String doPostToken(String url, String json,String token) {
		CloseableHttpClient httpClient = createClient();
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(json, CHARSET);
			stringEntity.setContentEncoding(CHARSET);
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("token", token);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET);
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeClient(httpClient);
		}
		return null;
	}

	/**
	 * HTTP Get 获取内容
	 *
	 * @param url
	 *            请求的url地址
	 * @param params
	 *            请求的参数
	 * @param token
	 *            编码格式
	 * @return
	 */
	public static String doGetToken(String url, Map<String, String> params, String token) {
		CloseableHttpClient httpClient = createClient();
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
			}
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Content-Type", "application/json");
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("token", token);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET);
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeClient(httpClient);
		}
		return null;
	}
	
}
