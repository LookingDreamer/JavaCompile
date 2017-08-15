package com.zzb.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzb.app.service.AppProvideFindService;


@Service
@Transactional
public class AppProvideFindServiceImpl implements
		AppProvideFindService {

	@Override
	public String provideFind(String providerId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> msm_header = new HashMap<String, String>();
		Map<String, Object> Body = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 添加MSM_HEADER数据
		// TODO :Add MSM_HEADER Data
		msm_header.put("CmdId","Provider");
		msm_header.put("CmdModule", "PROVIDER");
		msm_header.put("CmdType", "FHBX");
		msm_header.put("CmdVer", "100");
		msm_header.put("Token", "test");
		// 添加Body数据
		resultMap.put("quotationTime", "5");
		resultMap.put("region", "440100,440900");
		resultMap.put("interfaceType", "ManualTask");
		resultMap.put("failedInsureIntervention", false);
		resultMap.put("interfaceMode", "ManTradition");
		resultMap.put("productDescription", "");
		resultMap.put("checkFirst",false );
		resultMap.put("ediProcessor", null);
		resultMap.put("enable", "1");
		resultMap.put("insureInfo", null);
		resultMap.put("bindingLicense", "0");
		resultMap.put("paymentValidity", "0");
		resultMap.put("productFeatures", "");
		resultMap.put("approved", "1");
		resultMap.put("resultIntervention", false);
		resultMap.put("addon", true);
		resultMap.put("insureTime", "5");
		resultMap.put("quotationInterval", "5");
		resultMap.put("renewalItfMode", null);
		resultMap.put("insCompanyId", "525");
		resultMap.put("insureInfoReturnMode", "Manual");
		resultMap.put("directInsure", false);
		resultMap.put("insureWay", "human");
		resultMap.put("selfSelectLocation", true);
		resultMap.put("insBranchName", "大地财险广东");
		resultMap.put("insComCode", "202144");
		resultMap.put("regionName", "广州市,茂名市");
		resultMap.put("class", "com.baoxian.provider.inscar.model.BasePropsProvider");
		resultMap.put("trailAsActuary", "1");
		resultMap.put("seperateProcess", true);
		resultMap.put("relatedRule", "194");
		resultMap.put("tax", true);
		resultMap.put("insureSuccessInfo", null);
		resultMap.put("autoGetPaymentResult", false);
		resultMap.put("needVerificationCode", false);
		resultMap.put("providerId", "107");
		resultMap.put("quotationValidity", "10");
		resultMap.put("seperateInsure", true);
		resultMap.put("insCompanyName", "大地财险");
		List<Map<String, Object>> paymentMeanslist = new ArrayList<Map<String,Object>>();
		Map<String, Object> paymentMeans = new HashMap<String, Object>();
        //list中添加的是map list.add(map)
		paymentMeans.put("name", "转账");
		paymentMeans.put("code", "Transfer");
		paymentMeanslist.add(paymentMeans);
		List<Map<String, Object>> channellist = new ArrayList<Map<String,Object>>();
		Map<String, Object> channel = new HashMap<String, Object>();
		channel.put("channel", "B2B");
		channellist.add(channel);
		
		Map<String, Object> ProviderInfo = new HashMap<String, Object>();
		Body.put("Body", ProviderInfo);
		ProviderInfo.put("ProviderInfo",resultMap );
		
		result.put("MSM_HEADER", msm_header);
		result.put("Body", Body);
		result.put("Code", "0");
		result.put("Text", "OK");
		result.put("Type", "0");
		
		JSONObject json = JSONObject.fromObject(result);
		return json.toString();
		
	}
	
}