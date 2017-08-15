package com.zzb.app.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzb.app.service.AppVehicleService;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;

@SuppressWarnings("deprecation")
@Service
@Transactional
public class AppVehicleServiceImpl implements AppVehicleService {

	@Resource
	private INSBCarmodelinfoDao carmodelinfoDao;

	@Resource
	private INSBCarmodelinfohisDao carmodelinfohisDao;

	@Override
	public String queryVehicleByType(String header, String param) {
		String result="";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String[] vParamArray = null;
		try {
			vParamArray = param.split(";");
		} catch (Exception e1) {
			vParamArray[0]=param;
			e1.printStackTrace();
		}
		for (String str : vParamArray) {
			String[] tempArray = str.split("=");
			if (tempArray.length == 2) {
				paramMap.put(tempArray[0], tempArray[1]);
			} else if (tempArray.length == 1) {
				paramMap.put(tempArray[0], null);
			}
		}
		String strURL="http://cx.baoxan.org/searchList/";
		if(vParamArray.length==3){
			strURL =strURL+ paramMap.get("key") + "/" + paramMap.get("count") + "/"
					 + paramMap.get("off");
		}else{
			strURL =strURL+ paramMap.get("key");
		}
		System.out.println("=url=:" + strURL);

		result = getDataByUrl(strURL);
		return result;
	}

	@Override
	public String queryVehicleByBrandName(String header,
			String param) {
		String result="";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String[] vParamArray = null;
		vParamArray = param.split(";");
		for (String str : vParamArray) {
			String[] tempArray = str.split("=");
			if (tempArray.length == 2) {
				paramMap.put(tempArray[0], tempArray[1]);
			} else if (tempArray.length == 1) {
				paramMap.put(tempArray[0], null);
			}
		}
		String strURL="http://cx.baoxan.org/filter/";
		strURL =strURL+ paramMap.get("key");
		System.out.println("=url=:" + strURL);
		result = getDataByUrl(strURL);
		return result;
	}
	/**
	 * 通过http得到数据
	 * 
	 * @param strURL
	 * @return
	 */
	private String getDataByUrl(String strURL){
		String result="";
		@SuppressWarnings({ "resource" })
		HttpClient client = new DefaultHttpClient(); 
		HttpGet request = new HttpGet(strURL);
		HttpResponse response = null;
		BufferedReader rd = null;
		 String line = "";  
		try {
			response = client.execute(request);
			rd = new BufferedReader(  
			        new InputStreamReader(response.getEntity().getContent()));
			response.getStatusLine().getStatusCode();  
			while((line = rd.readLine()) != null) {  
				result = result+line;
			}
			 System.out.println("==="+result);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return result;
	}
}