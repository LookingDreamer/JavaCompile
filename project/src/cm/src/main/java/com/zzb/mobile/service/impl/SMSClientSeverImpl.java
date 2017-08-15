package com.zzb.mobile.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzb.mobile.service.SMSClientService;
import com.zzb.mobile.util.EncodeUtils;
import com.zzb.mobile.util.SMSClient;

@Service
@Transactional
public class SMSClientSeverImpl implements SMSClientService{

	@Override
	public void sendMobileValidateCode(String phone, String code) throws Exception {
		try{
			ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
			String url = resourceBundle.getString("smsclient.send.url");
			String sn = resourceBundle.getString("smsclient.send.sn");
			String pwd = resourceBundle.getString("smsclient.send.pwd");
			String msg = resourceBundle.getString("smsclient.sendmsg.msg");
			SMSClient.SendMsg(url,sn,pwd,msg,phone,code);
		}catch(Exception e){
			throw e;
		}
	}
	
	
	@Override
	public void sendRegistSuccessMessage(String phone, String username, String password) throws Exception {
		try{
			ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
			String url = resourceBundle.getString("smsclient.send.url");
			String sn = resourceBundle.getString("smsclient.send.sn");
			String pwd = resourceBundle.getString("smsclient.send.pwd");
			String msg = resourceBundle.getString("smsclient.sendmsg.msgRegist");
			SMSClient.SendMsg(url,sn,pwd,msg,phone,username,password);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public void sendUpdateSuccessMessage(String phone, String username) throws Exception {
		try {
			ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
			String url = resourceBundle.getString("smsclient.send.url");
			String sn = resourceBundle.getString("smsclient.send.sn");
			String pwd = resourceBundle.getString("smsclient.send.pwd");
			String msg = resourceBundle.getString("smsclient.sendmsg.pwdUpdate");
			SMSClient.SendMsg(url, sn, pwd, msg, phone, username);
		} catch (Exception e) {
			throw e;
		}
	}

	private HashMap<String,String> initParam(String phone, String code,ResourceBundle prop)
			throws UnsupportedEncodingException, IOException{
		Date now = new Date();
		now.setMinutes(now.getMinutes()+1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sendtime = sdf.format(now);
		
		String msg = prop.getString("smsclient.sendmsg.msg");
		String act = prop.getString("smsclient.sendmsg.act");
		String unitid = prop.getString("smsclient.sendmsg.unitid");
		String username = prop.getString("smsclient.sendmsg.username");
		String passwd = prop.getString("smsclient.sendmsg.passwd");
		passwd = EncodeUtils.encodeMd5(passwd);
		msg=msg.replace("{code}", code);
		
	//	String param=prop.getProperty("smsclient.sendmsg.param");
		HashMap<String,String> param = new HashMap<String,String>();
		param.put("act", act);
		param.put("unitid", unitid);
		param.put("username", username);
		param.put("phone", phone);
		param.put("passwd", passwd);
		param.put("msg", msg);
		param.put("sendtime", sendtime);
		param.put("port", "");
		return param;
	}
	
	
	private static String invoke(HttpClient httpclient, HttpUriRequest httpost) {  
        HttpResponse response = sendRequest(httpclient, httpost);  
        String body = paseResponse(response);  
        
        return body;  
    }
	
	
    private static String paseResponse(HttpResponse response) {   
        HttpEntity entity = response.getEntity();   
        String charset = "UTF-8";  
        String body = null;  
        try {  
            body = EntityUtils.toString(entity,charset);  

        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }   
        return body;  
    }  
	  
    private static HttpResponse sendRequest(HttpClient httpclient, HttpUriRequest httpost) {  
        HttpResponse response = null;  
          
        try {  
            response = httpclient.execute(httpost);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return response;  
    }  
	  
	private HttpPost postForm(String url,Map<String, String> params) {
		 HttpPost httpost = new HttpPost(url);  
		 List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
		   
		 Set<String> keySet = params.keySet();  
		 for(String key : keySet) {  
		     nvps.add(new BasicNameValuePair(key, params.get(key)));  
		 }  
		   
		 try {  
		     httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		 } catch (UnsupportedEncodingException e) {  
		     e.printStackTrace();  
		 }
		 return httpost;
	}

	
}
