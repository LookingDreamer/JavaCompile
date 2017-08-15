package com.zzb.chn.service;

import java.util.Map;

import com.zzb.chn.bean.QuoteBean;
import com.zzb.mobile.model.CommonModel;

public interface CHNMerchantService {
	public CommonModel getProviders(QuoteBean quoteBeanIn) throws Exception;
	public CommonModel getInterCallDetail(Map<String, Object> mapIn) throws Exception;
	public CommonModel getInterCallDayDetail(Map<String, Object> mapIn) throws Exception;
	public CommonModel querySummaryCount(Map<String, Object> mapIn) throws Exception;
	public CommonModel queryAgreementArea(Map<String, Object> mapIn) throws Exception ;
	public CommonModel getChannels(Map<String, Object> mapIn) throws Exception;

	public CommonModel getOldCarTaskList(Map<String, Object> mapIn);
	public CommonModel buildCountData(Map<String, Object> mapIn) throws Exception;
} 
 