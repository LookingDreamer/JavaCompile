package com.zzb.app.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.HttpClientUtil;
import com.zzb.app.service.AppCloudQueryService;

@Service
@Transactional
public class AppCloudQueryServiceImpl implements AppCloudQueryService{

	private static String url = "http://10.88.12.212:8088/cif/rnpolicy/queryLastYearPolicy";
	
	@Override
	public String queryByNumber(String type, String insureno) {
		Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        map.put("insureno", insureno);
        String postData = "";
		try {
			postData = HttpClientUtil.doPost(url,map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postData;
	}

	@Override
	public String queryByCarInfo(String framenumber, String enginenumber) {
		Map<String, String> map = new HashMap<String, String>();
        map.put("framenumber", framenumber);
        map.put("enginenumber", enginenumber);
        String postData = "";
		try {
			postData = HttpClientUtil.doPost(url,map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return postData;
	}

}
