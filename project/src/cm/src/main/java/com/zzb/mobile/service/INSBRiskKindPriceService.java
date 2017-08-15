package com.zzb.mobile.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.conf.entity.INSBRisk;

public interface INSBRiskKindPriceService extends BaseService<INSBCarkindprice>{
	public List<INSBCarkindprice> allKindPrice(String processInstanceId,String inscomcode);
	
	public List<Map<String,Object>> riskKindByCode(String insKindCode);
	
	public String allDtailesKind(String processInstanceId,String inscomcode);
	
	//public INSBCarkindprice kindPrice(String processInstanceId,String insKindCode);
	public List<INSBCarkindprice> kindPrice(String processInstanceId,String insKindCode);
	
	public void updateKindPriceItem(INSBCarkindprice carkindprice);
	
	public String kindPriceList(String processInstanceId, String insbcomcode);
}
