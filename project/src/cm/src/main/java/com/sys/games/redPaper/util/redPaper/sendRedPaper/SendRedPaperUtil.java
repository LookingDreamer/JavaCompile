package com.sys.games.redPaper.util.redPaper.sendRedPaper;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.sys.games.redPaper.entity.Account;
import com.sys.games.redPaper.entity.OpenID;


public class SendRedPaperUtil {

	public static  JSONObject doPostStrJson(String url,String outStr){
		DefaultHttpClient httpClient=new DefaultHttpClient();
		HttpPost httpPost=new HttpPost(url);	
		JSONObject jsonObject=null;
		try {
			 StringEntity s = new StringEntity(outStr);  
			 s.setContentEncoding("UTF-8");    
			 s.setContentType("application/json");
			 httpPost.setEntity(s);
			HttpResponse response=httpClient.execute(httpPost);
			String result=EntityUtils.toString(response.getEntity(),"UTF-8");
			//System.out.println("result: "+result);
			jsonObject=JSONObject.fromObject(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	
	//根据openid查出用户信息
	public static Account getAccountByOpenid(String _openid,String _amount){
		String _url="https://weixin.52zzb.com/online/wxOpenid";
		Account account = null;//用户对象
		OpenID idObj=new OpenID();
		idObj.setOpenid(_openid);
		String id_josnStr=JSONObject.fromObject(idObj).toString();
		try {
			JSONObject jsonObj=doPostStrJson(_url,id_josnStr);
			if((jsonObj!=null)&&(!("".equals(jsonObj))))//不为空
			{
			      boolean  state  =(Boolean) jsonObj.get("state");
			      
			      System.out.println("state: "+state);
			      
			      if(!!state){//查询成功
			    	  JSONObject attributes= JSONObject.fromObject((JSONObject.fromObject(jsonObj.get("entity")).get("Attributes")));
			    	  
			    	  System.out.println("attributes: ");
			    	  System.out.print(attributes.toString());
			    	  
			    	  JSONObject org = JSONObject.fromObject((JSONObject.fromObject(jsonObj.get("entity")).get("org")));
			    	  String region = (String) (JSONObject.fromObject(jsonObj.get("entity")).get("region"));
			    	  String employeeNumber=(String)attributes.get("employeeNumber");
			    	  String fullName=(String) attributes.get("fullName");
			    	  String cityId=(String) attributes.get("cityId");
			    	  String mobile=(String) attributes.get("mobile");
			    	  String platform=(String) org.get("platform");
			    	  String plateName=(String) org.get("plateName");	
			    	  if(region==null){
			    		  region="";
			    		  System.out.println("region==null");
			    	  }
                      if(employeeNumber==null){
                    	  employeeNumber="";
                    	  System.out.println("employeeNumber==null");
			    	  }  
                      if(fullName==null){
                    	  fullName="";
                    	  System.out.println("fullName==null");
			    	  }
                      
                      if(cityId==null){
                    	  cityId="";
                    	  System.out.println("cityId==null");
			    	  }
                      if(mobile==null){
                    	  mobile="";
                    	  System.out.println("mobile==null");
			    	  }
                      if(platform==null){
                    	  platform="";
                    	 System.out.println("platform==null");
                      }
                      if(plateName==null){
                    	  plateName="";
                    	  System.out.println("plateName==null");
                       }
                      
                      
			    	  account=new Account(employeeNumber,fullName,mobile,platform,plateName,_openid,cityId,region,_amount);	 
			      } 
		   }
		} catch (Exception e) {
			e.printStackTrace();
			return account;
		}
		return account;
	}
}
