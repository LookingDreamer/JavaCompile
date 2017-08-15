package com.common;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpClient
 *
 * @author huan
 */
public class HttpSender {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("HttpSender");
    private static final int SOCKET_TIMEOUT = 90000;        //响应超时时间
    private static final String CHARSET = "UTF-8";          //默认UTF-8编码
    public static final String HTTP_GET_METHOD = "get";
    public static final String HTTP_POST_METHOD = "post";
    public static final String HTTP_PUT_METHOD = "put";
    public static final String HTTP_HEAD_METHOD = "head";

    private static PoolingHttpClientConnectionManager ccm;
    private static HashMap<String, String> HEADERS = new HashMap<String, String>() {
        {
            put("Content-Type", "application/json;charset=utf-8");
        }
    };
    private static LayeredConnectionSocketFactory sslSocketFactory = null;

    private static RequestConfig defaultRequestConfig = null;

    public static int defaultConnectTimeout = 90000;
    public static int defaultReadTimeout = 90000;

    private static final String defaultHttpClientSocketTimeoutKey = "defaultHttpClientSocketTimeout";
    public static int defaultHttpClientSocketTimeout = 15000;
    private static final String defaultHttpClientConnectTimeoutKey = "defaultHttpClientConnectTimeout";
    public static int defaultHttpClientConnectTimeout = 15000;
    private static final String defaultHttpClientConnectionRequestTimeoutKey = "defaultHttpClientConnectionRequestTimeout";
    public static int defaultHttpClientConnectionRequestTimeout = 15000;

    static {
        try {
            logger.info("设置前系统socket连接时间：sun.net.client.defaultConnectTimeout==" + System.getProperty("sun.net.client.defaultConnectTimeout"));
            logger.info("设置前系统socket连接时间：sun.net.client.defaultReadTimeout==" + System.getProperty("sun.net.client.defaultReadTimeout"));
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(defaultConnectTimeout));// （单位：毫秒）
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(defaultReadTimeout)); // （单位：毫秒）
            defaultRequestConfig = RequestConfig.custom()
                    .setSocketTimeout(defaultHttpClientSocketTimeout)
                    .setConnectTimeout(defaultHttpClientConnectTimeout)
                    .setConnectionRequestTimeout(defaultHttpClientConnectionRequestTimeout)
                    .setStaleConnectionCheckEnabled(true)
                    .build();
            logger.info("设置后系统socket连接时间：sun.net.client.defaultConnectTimeout==" + System.getProperty("sun.net.client.defaultConnectTimeout"));
            logger.info("设置后系统socket连接时间：sun.net.client.defaultReadTimeout==" + System.getProperty("sun.net.client.defaultReadTimeout"));
//            System.setProperty("https.protocols", "SSLv2Hello,SSLv3,TLSv1,TLSv1.1,TLSv1.2");
            //http明文报文Socket工厂
            ConnectionSocketFactory plainSf = PlainConnectionSocketFactory.getSocketFactory();
            //加密上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, null);


            sslSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1", "SSLv3", "SSLv2Hello"},
                    null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            LayeredConnectionSocketFactory sslSocketFactory1 = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainSf).register("https", sslSocketFactory).build();
            ccm = new PoolingHttpClientConnectionManager(r);
            //like https.protocols=SSLv3,TLSv1) it will start with TLSv1.

            ccm.setMaxTotal(300); //将最大连接数增加到300
            ccm.setDefaultMaxPerRoute(20); //将每个路由基础的连接增加到20
            ccm.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(defaultReadTimeout).build());
        } catch (Exception e) {
            logger.error("HTTPCLIENT 初始化异常 ", e);
            throw new RuntimeException("HTTPCLIENT 初始化异常");
        }
    }
    private static Map<String,LayeredConnectionSocketFactory> sslSocketFactoryMap=new HashMap<String,LayeredConnectionSocketFactory>();
    public static CloseableHttpClient buildHttpClientByComId(String insComId){
        try {
            if(sslSocketFactoryMap.containsKey(insComId))
            {

            }
            else {
                String defaultPwd="123456";
                SSLContext sslContext2 = SSLContext.getInstance("SSL");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                TrustManagerFactory tmf = TrustManagerFactory
                        .getInstance("SunX509");
                KeyStore ks = KeyStore.getInstance("JKS");
                String path=HttpSender.class.getResource("/keys/"+insComId+".jks").getFile();
                ks.load(new FileInputStream(path),
                        defaultPwd.toCharArray());

                kmf.init(ks, defaultPwd.toCharArray());
                KeyStore tks = KeyStore.getInstance("JKS");
                tks.load(new FileInputStream(path),
                        defaultPwd.toCharArray());
                tmf.init(tks);
                sslContext2.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                LayeredConnectionSocketFactory sslSocketFactory2 = new SSLConnectionSocketFactory(
                        sslContext2);
                sslSocketFactoryMap.put(insComId,sslSocketFactory2);

            }
            return HttpClients.custom().setSSLSocketFactory(sslSocketFactoryMap.get(insComId)).build();

        }
        catch (Exception ex)
        {
            logger.error("加载专用的http请求出现异常",ex);
            return HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
        }
    }


    public static CloseableHttpClient buildHttpClient() {
        return buildHttpClient(true,"");
    }

    public static CloseableHttpClient buildHttpClient(boolean usePoolConnect,String url) {
        if(url!=null&&url.length()>0&&url.startsWith("https://ywtest.ccic-net.com.cn"))
        {
            return buildHttpClientByComId("dadi-test");
        }
        else if (usePoolConnect)
            return HttpClients.custom().setConnectionManager(ccm).build();
        else {
            return HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
        }
    }

    public static String doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, params, CHARSET);
    }

    public static String doPost(String url, String sendStr) throws Exception {
        return doPost(true, url, sendStr, null, HEADERS, CHARSET, getDefaultRequestConfig());
    }

    /**
     * Http Get 获取内容
     *
     * @param url     请求的url地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 响应内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) throws Exception {
        return doGet(buildHttpClient(), url, params, charset);
    }

    public static RequestConfig getDefaultRequestConfig() {
        return defaultRequestConfig;
    }

    public static String doGet(CloseableHttpClient httpClient, String url, Map<String, String> params, String charset) throws Exception {
        return doGet(httpClient, url, params, charset, getDefaultRequestConfig());
    }

    public static String doGet(CloseableHttpClient httpClient, String url, Map<String, String> params, String charset, RequestConfig requestConfig) throws Exception {
        if (url==null||url.length()==0) return null;
        CloseableHttpResponse response = null;
        String result = null;
        HttpGet httpGet = null;
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                if (url.contains("?")) {
                    url += "&" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
                } else {
                    url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
                }
            }
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200 && statusCode != 500) {
                httpGet.abort();
                throw new Exception("HttpClient,Error Status Code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            if (httpGet != null && !httpGet.isAborted()) {
                httpGet.abort();
            }
            logger.error("HttpClient Get Error:", e);
            Exception exception = new Exception("HttpClient 请求异常！", e);
            throw exception;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("回收httpConnection时发生异常", e);
                }
            }
            if (httpGet != null) {
                try {
                    httpGet.releaseConnection();
                } catch (Exception e) {
                    logger.error("释放httpConnection时发生异常", e);
                }
            }
        }
    }

    public static String doPost(String url, String sendStr, Map<String, String> headers, String charset) throws Exception {
        return doPost(true, url, sendStr, null, headers, charset, defaultRequestConfig);
    }

    public static String doPost(String url, String sendStr, Map<String, String> headers, String charset, RequestConfig requestConfig) throws Exception {
        return doPost(true, url, sendStr, null, headers, charset, requestConfig);
    }

    public static String doPost(String url, String sendStr, Map<String, String> params, Map<String, String> headers, String charset) throws Exception {
        return doPost(true, url, sendStr, params, headers, charset, defaultRequestConfig);
    }

    public static String doPost(String url, String sendStr, Map<String, String> params, Map<String, String> headers, String charset, RequestConfig requestConfig) throws Exception {
        return doPost(true, url, sendStr, params, headers, charset, requestConfig);
    }

    /**
     * Http Post 获取内容
     *
     * @param usePoolConnect 是否需要缓存连接
     * @param url            请求的url地址
     * @param sendStr        请求的参数
     * @param params         请求头文件
     * @param charset        编码格式
     * @return 响应内容
     */
    public static String doPost(boolean usePoolConnect, String url, String sendStr, Map<String, String> params, Map<String, String> headers, String charset) throws Exception {
        return doPost(usePoolConnect, url, sendStr, params, headers, charset, getDefaultRequestConfig());

    }


    /**
     * Http Post 获取内容
     *
     * @param usePoolConnect 是否需要缓存连接
     * @param url            请求的url地址
     * @param sendStr        请求的参数
     * @param params         请求头文件
     * @param charset        编码格式
     * @return 响应内容
     */
    public static String doPost(boolean usePoolConnect, String url, String sendStr, Map<String, String> params, Map<String, String> headers, String charset, RequestConfig requestConfig) throws Exception {
        if (url==null||url.length()==0) return null;
        CloseableHttpClient httpClient = buildHttpClient(usePoolConnect,url);
        CloseableHttpResponse response = null;
        String result = null;
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            if (headers != null) {
                for (String header : headers.keySet()) {
                    httpPost.setHeader(header, headers.get(header));
                }
            }
            if (params != null && params.size() > 0) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
            } else {
                httpPost.setEntity(new StringEntity(sendStr, charset));
            }
            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200 && statusCode != 500) {
                httpPost.abort();
                throw new Exception("http request had failed , failed statusCode :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            if(httpPost!= null && !httpPost.isAborted()){
                httpPost.abort();
            }
            logger.error("HttpClient Post Error:", e);
            throw e;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("回收httpConnection时发生异常", e);
                }
            }
            if (httpPost != null) {
                try {
                    httpPost.releaseConnection();
                } catch (Exception e) {
                    logger.error("释放httpConnection时发生异常", e);
                }
            }
            if (!usePoolConnect && httpClient != null) {
                try {
                    httpClient.close();
                    logger.debug("释放链接httpClient");
                } catch (Exception e) {
                    logger.error("释放链接httpClient时发生异常", e);
                }
            }


        }
        return result;
    }

}
